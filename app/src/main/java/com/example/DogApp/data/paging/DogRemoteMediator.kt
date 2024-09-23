package com.example.DogApp.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.DogApp.data.local.DogDatabase
import com.example.DogApp.data.remote.DogApi
import com.example.DogApp.model.Dog
import com.example.DogApp.model.DogRemoteKeys
import com.example.DogApp.util.Constants.ITEMS_PER_PAGE

@ExperimentalPagingApi
class DogRemoteMediator(
    private val dogApi: DogApi,
    private val dogDatabase: DogDatabase
) : RemoteMediator<Int, Dog>() {

    private val dogDao = dogDatabase.dogDao()
    private val dogRemoteKeysDao = dogDatabase.dogRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Dog>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = dogApi.getDogs(page = currentPage, perPage = ITEMS_PER_PAGE)
            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            dogDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dogDao.deleteAllDogs()
                    dogRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.map { dog ->
                    DogRemoteKeys(
                        id = dog.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                dogRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                dogDao.addDogs(dogs = response)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.d("DogRemoteMediator", e.toString())
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Dog>
    ): DogRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                dogRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Dog>
    ): DogRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { dog ->
                dogRemoteKeysDao.getRemoteKeys(id = dog.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Dog>
    ): DogRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { dog ->
                dogRemoteKeysDao.getRemoteKeys(id = dog.id)
            }
    }

}