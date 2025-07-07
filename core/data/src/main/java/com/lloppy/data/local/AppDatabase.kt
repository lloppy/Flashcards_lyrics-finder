package com.lloppy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lloppy.data.local.dao.FlashcardDao
import com.lloppy.data.local.entity.FlashcardEntity

@Database(entities = [FlashcardEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flashcard_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}