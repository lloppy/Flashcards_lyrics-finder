package com.lloppy.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lloppy.data.local.dao.FlashcardDao
import com.lloppy.data.local.entity.FlashcardEntity
import com.lloppy.data.local.migration.versions.Migration1To2

@Database(entities = [FlashcardEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = buildDatabase(context)
                INSTANCE = instance
                instance
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = AppDatabase::class.java,
                name = "flashcard_database"
            )
                //.addTypeConverter(Converters())
                .fallbackToDestructiveMigration(false)
                .addMigrations(
                    Migration1To2()
                    // add more migration here
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.i("flashcard_database", "flashcard_database is created")
                    }
                })
                .build()
        }
    }
}