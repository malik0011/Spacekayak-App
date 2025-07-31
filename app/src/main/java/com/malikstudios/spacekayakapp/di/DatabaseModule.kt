package com.malikstudios.spacekayakapp.di

import android.content.Context
import androidx.room.Room
import com.malikstudios.spacekayakapp.data.local.AppDatabase
import com.malikstudios.spacekayakapp.data.local.dao.ServerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "server_db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideServerDao(db: AppDatabase): ServerDao = db.serverDao()
}
