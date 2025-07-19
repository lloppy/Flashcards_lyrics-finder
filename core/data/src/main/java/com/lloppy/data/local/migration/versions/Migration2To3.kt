package com.lloppy.data.local.migration.versions

import androidx.sqlite.db.SupportSQLiteDatabase
import com.lloppy.data.local.migration.MigrationTemplate
import com.lloppy.data.local.migration.strategies.AddColumnMigration

class Migration2To3 : MigrationTemplate(2, 3) {
    override fun executeMigration(db: SupportSQLiteDatabase) {
        AddColumnMigration(
            tableName = "notes",
            columnName = "priority",
            columnType = "INTEGER",
            defaultValue = "0"
        ).migrate(db)
    }
}