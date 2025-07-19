package com.lloppy.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.util.Log

const val MIGRATION_TAG = "DatabaseMigration"

abstract class MigrationTemplate(startVersion: Int, endVersion: Int) :
    Migration(startVersion, endVersion) {

    final override fun migrate(db: SupportSQLiteDatabase) {
        Log.i(MIGRATION_TAG, "Starting migration from v$startVersion to v$endVersion")

        db.beginTransaction()
        try {
            logDatabaseStats(db, "Before migration")

            preMigrate(db)
            executeMigration(db)
            postMigrate(db)

            logDatabaseStats(db, "After migration")
            db.setTransactionSuccessful()

            Log.i(MIGRATION_TAG, "Migration completed successfully")
        } catch (e: Exception) {
            Log.e(MIGRATION_TAG, "Migration failed", e)
            throw e
        } finally {
            db.endTransaction()
        }
    }

    protected open fun preMigrate(db: SupportSQLiteDatabase) {
        Log.d(MIGRATION_TAG, "Running pre-migration checks")
        validateDatabaseVersion(db)
    }

    protected abstract fun executeMigration(db: SupportSQLiteDatabase)

    protected open fun postMigrate(db: SupportSQLiteDatabase) {
        Log.d(MIGRATION_TAG, "Running post-migration validation")
        validateMigrationSuccess(db)
    }

    private fun validateDatabaseVersion(db: SupportSQLiteDatabase) {
        val version = db.version
        if (version != startVersion) {
            val errorMsg = "Invalid DB version: expected $startVersion, actual $version"
            Log.e(MIGRATION_TAG, errorMsg)
            throw IllegalStateException(errorMsg)
        }
    }

    private fun validateMigrationSuccess(db: SupportSQLiteDatabase) {
        val newVersion = db.version
        if (newVersion != endVersion) {
            val errorMsg = "Migration failed: DB version should be $endVersion, but is $newVersion"
            Log.e(MIGRATION_TAG, errorMsg)
            throw IllegalStateException(errorMsg)
        }
    }

    private fun logDatabaseStats(db: SupportSQLiteDatabase, prefix: String) {
        try {
            val tablesCursor = db.query("SELECT name FROM sqlite_master WHERE type='table'")
            val tables = mutableListOf<String>()
            while (tablesCursor.moveToNext()) {
                tables.add(tablesCursor.getString(0))
            }
            tablesCursor.close()

            Log.d(
                MIGRATION_TAG,
                "$prefix:\n" +
                        "  Version: ${db.version}\n" +
                        "  Tables: ${tables.joinToString()}\n" +
                        "  Path: ${db.path}"
            )
        } catch (e: Exception) {
            Log.w(MIGRATION_TAG, "Failed to log database stats", e)
        }
    }
}