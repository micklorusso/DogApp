package com.example.DogApp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.DogApp.data.local.dao.DogDao
import com.example.DogApp.data.local.dao.DogRemoteKeysDao
import com.example.DogApp.model.Dog
import com.example.DogApp.model.DogRemoteKeys

@Database(entities = [Dog::class, DogRemoteKeys::class], version = 3)
abstract class DogDatabase : RoomDatabase() {

    abstract fun dogDao(): DogDao
    abstract fun dogRemoteKeysDao(): DogRemoteKeysDao

}