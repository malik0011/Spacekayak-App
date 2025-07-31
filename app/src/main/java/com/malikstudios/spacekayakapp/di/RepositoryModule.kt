package com.malikstudios.spacekayakapp.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import com.malikstudios.spacekayakapp.data.local.AppDatabase
import com.malikstudios.spacekayakapp.data.local.LocalDataSource
import com.malikstudios.spacekayakapp.data.local.dao.ServerDao
import com.malikstudios.spacekayakapp.domain.repository.ServerRepository
import com.malikstudios.spacekayakapp.domain.repository.ServerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideServerRepository(
        localDataSource: LocalDataSource,
        @ApplicationContext context: Context
    ): ServerRepository {
        FirebaseApp.initializeApp(context) //

        val firestore = Firebase.firestore
        return ServerRepositoryImpl(localDataSource, firestore, context)
    }
}
