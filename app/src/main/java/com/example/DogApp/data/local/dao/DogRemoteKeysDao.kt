package com.example.DogApp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.DogApp.model.DogRemoteKeys

@Dao
interface DogRemoteKeysDao {

    @Query("SELECT * FROM dog_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id: Int): DogRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<DogRemoteKeys>)

    @Query("DELETE FROM dog_remote_keys_table")
    suspend fun deleteAllRemoteKeys()

}