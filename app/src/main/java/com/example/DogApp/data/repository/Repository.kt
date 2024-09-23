package com.example.DogApp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.DogApp.data.local.DogDatabase
import com.example.DogApp.data.paging.SearchPagingSource
import com.example.DogApp.data.paging.DogRemoteMediator
import com.example.DogApp.data.remote.DogApi
import com.example.DogApp.model.Dog
import com.example.DogApp.util.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class Repository @Inject constructor(
    private val dogApi: DogApi,
    private val dogDatabase: DogDatabase
) {

    fun getAllDogs(): Flow<PagingData<Dog>> {
        val pagingSourceFactory = { dogDatabase.dogDao().getAllDogs() }
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = DogRemoteMediator(
                dogApi = dogApi,
                dogDatabase = dogDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun searchDogs(query: String): Flow<PagingData<Dog>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                SearchPagingSource(dogApi = dogApi, query = query)
            }
        ).flow
    }

}