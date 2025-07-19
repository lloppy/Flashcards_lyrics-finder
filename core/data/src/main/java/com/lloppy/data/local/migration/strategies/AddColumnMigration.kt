package com.lloppy.data.local.migration.strategies

import androidx.sqlite.db.SupportSQLiteDatabase

class AddColumnMigration(
    private val tableName: String,
    private val columnName: String,
    private val columnType: String,
    private val defaultValue: String? = null
) : MigrationStrategy {
    override fun migrate(db: SupportSQLiteDatabase) {
        val defaultClause = defaultValue?.let { "DEFAULT $it" } ?: ""
        db.execSQL("ALTER TABLE $tableName ADD COLUMN $columnName $columnType $defaultClause")
    }
}