package com.example.bookshelf.network

import com.example.bookshelf.model.BookSearchResponse
import com.example.bookshelf.model.BookVolume
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for querying the Google Books REST API.
 */
interface BookshelfApiService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BookSearchResponse

    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") id: String): BookVolume
}
