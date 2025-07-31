package com.malikstudios.spacekayakapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.malikstudios.spacekayakapp.data.local.dao.ServerDao
import com.malikstudios.spacekayakapp.data.local.entities.ServerEntity

// data/local/AppDatabase.kt

@Database(
    entities = [ServerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
}
