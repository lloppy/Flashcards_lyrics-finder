package com.lloppy.data.local.migration.strategies

import androidx.sqlite.db.SupportSQLiteDatabase

interface MigrationStrategy {
    fun migrate(db: SupportSQLiteDatabase)
}
