package com.example.DogApp.data.remote

import com.example.DogApp.model.Dog
import com.example.DogApp.util.Constants.BREEDS_ENDPOINT
import com.example.DogApp.util.Constants.SEARCH_ENDPOINT
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApi {

    @GET(BREEDS_ENDPOINT)
    suspend fun getDogs(
        @Query("page") page: Int,
        @Query("limit") perPage: Int
    ): List<Dog>

    @GET(SEARCH_ENDPOINT)
    suspend fun searchDogs(
        @Query("q") query: String,
    ): List<Dog>

}