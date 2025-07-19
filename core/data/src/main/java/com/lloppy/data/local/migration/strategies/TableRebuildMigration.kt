package com.lloppy.data.local.migration.strategies

import androidx.sqlite.db.SupportSQLiteDatabase

class TableRebuildMigration(
    private val tableName: String,
    private val newColumns: List<Pair<String, String>>,
    private val indexQueries: List<String> = emptyList()
) : MigrationStrategy {
    override fun migrate(db: SupportSQLiteDatabase) {
        val tempTable = "${tableName}_new"

        // 1. Создаем временную таблицу
        val columnsSql = newColumns.joinToString(", ") { "${it.first} ${it.second}" }
        db.execSQL("CREATE TABLE $tempTable ($columnsSql)")

        // 2. Копируем данные
        val oldColumns = newColumns.map { it.first }.filterNot { it == "created_at" }
        db.execSQL("INSERT INTO $tempTable (${oldColumns.joinToString(", ")}) " +
                "SELECT ${oldColumns.joinToString(", ")} FROM $tableName")

        // 3. Удаляем старую таблицу
        db.execSQL("DROP TABLE $tableName")

        // 4. Переименовываем
        db.execSQL("ALTER TABLE $tempTable RENAME TO $tableName")

        // 5. Создаем индексы
        indexQueries.forEach { db.execSQL(it) }
    }
}