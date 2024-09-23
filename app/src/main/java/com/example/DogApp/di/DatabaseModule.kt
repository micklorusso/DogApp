package com.example.DogApp.di

import android.content.Context
import androidx.room.Room
import com.example.DogApp.data.local.DogDatabase
import com.example.DogApp.util.Constants.DOG_DATABASE
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DogDatabase {
        return Room.databaseBuilder(
            context,
            DogDatabase::class.java,
            DOG_DATABASE
        ).build()
    }

}