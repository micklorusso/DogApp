package com.example.DogApp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.DogApp.model.Dog

@Dao
interface DogDao {

    @Query("SELECT * FROM dog_table")
    fun getAllDogs(): PagingSource<Int, Dog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDogs(dogs: List<Dog>)

    @Query("DELETE FROM dog_table")
    suspend fun deleteAllDogs()

}