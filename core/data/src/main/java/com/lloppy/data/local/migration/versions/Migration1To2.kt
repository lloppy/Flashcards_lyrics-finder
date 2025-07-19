package com.lloppy.data.local.migration.versions

import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lloppy.data.local.migration.MIGRATION_TAG
import com.lloppy.data.local.migration.MigrationTemplate
import com.lloppy.data.local.migration.strategies.TableRebuildMigration

class Migration1To2 : MigrationTemplate(1, 2) {
    override fun executeMigration(db: SupportSQLiteDatabase) {
        Log.i(MIGRATION_TAG, "Executing complex table rebuild migration")

        try {
            TableRebuildMigration(
                tableName = "notes",
                newColumns = listOf(
                    "id" to "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL",
                    "name" to "TEXT NOT NULL",
                    "description" to "TEXT NOT NULL",
                    "created_at" to "TEXT DEFAULT CURRENT_TIMESTAMP"
                ),
                indexQueries = listOf(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_notes_name_description " +
                            "ON notes (name, description)"
                )
            ).migrate(db)

            Log.d(MIGRATION_TAG, "Table rebuild completed successfully")
        } catch (e: Exception) {
            Log.e(MIGRATION_TAG, "Table rebuild failed", e)
            throw e
        }
    }

    override fun postMigrate(db: SupportSQLiteDatabase) {
        super.postMigrate(db)

        val hasCreatedAt = try {
            db.query("SELECT created_at FROM notes LIMIT 1")
            true
        } catch (e: Exception) {
            false
        }

        if (!hasCreatedAt) {
            Log.e(MIGRATION_TAG, "New column 'created_at' not found after migration")
            throw IllegalStateException("Migration failed - new column missing")
        }
    }
}