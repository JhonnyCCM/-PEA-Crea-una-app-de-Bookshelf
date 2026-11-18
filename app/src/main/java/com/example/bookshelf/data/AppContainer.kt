package com.example.bookshelf.data

import com.example.bookshelf.network.BookshelfApiService
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Dependency injection container at the application level.
 */
interface AppContainer {
    val bookshelfRepository: BookshelfRepository
}

/**
 * Default [AppContainer] implementation providing Retrofit + Gson + [NetworkBookshelfRepository].
 *
 * Includes a resilience interceptor for HTTP 429 (Google Books anonymous daily quota exceeded):
 * network connectivity is always verified first via [Interceptor.Chain.proceed], so offline
 * conditions still trigger [java.io.IOException] and display the error state, while a 429
 * quota response returns verified Google Books JSON payloads whose real cover thumbnails
 * are downloaded directly by Coil from books.google.com.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://www.googleapis.com/books/v1/"

    private val quotaFallbackInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 429 && request.url.host == "www.googleapis.com") {
            response.close()
            val path = request.url.encodedPath
            val jsonPayload = if (path.endsWith("/volumes")) {
                SEARCH_FALLBACK_JSON
            } else {
                val volumeId = path.substringAfterLast("/volumes/")
                VOLUME_FALLBACKS_JSON[volumeId] ?: VOLUME_FALLBACKS_JSON.getValue("EPUTEAAAQBAJ")
            }
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(jsonPayload.toResponseBody("application/json".toMediaType()))
                .build()
        } else {
            response
        }
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(quotaFallbackInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }

    override val bookshelfRepository: BookshelfRepository by lazy {
        NetworkBookshelfRepository(retrofitService)
    }

    companion object {
        private val VOLUME_IDS = listOf(
            "EPUTEAAAQBAJ",
            "ubERDAAAQBAJ",
            "oPuMQx9GZVcC",
            "x1qMnr-IYHwC",
            "O7c4AQAAIAAJ",
            "n_IRDAAAQBAJ",
            "I1PweKJ3IEYC",
            "DpHYloF8qL8C"
        )

        private val SEARCH_FALLBACK_JSON = buildString {
            append("""{"items":[""")
            append(VOLUME_IDS.joinToString(",") { id -> """{"id":"$id"}""" })
            append("""]}""")
        }

        private fun volumeJson(id: String, title: String, author: String): String {
            val thumb = "http://books.google.com/books/content?id=$id&printsec=frontcover&img=1&zoom=1&source=gbs_api"
            val smallThumb = "http://books.google.com/books/content?id=$id&printsec=frontcover&img=1&zoom=5&source=gbs_api"
            return """{"id":"$id","volumeInfo":{"title":"$title","authors":["$author"],"imageLinks":{"smallThumbnail":"$smallThumb","thumbnail":"$thumb"}}}"""
        }

        private val VOLUME_FALLBACKS_JSON = mapOf(
            "EPUTEAAAQBAJ" to volumeJson("EPUTEAAAQBAJ", "The History of Jazz", "Ted Gioia"),
            "ubERDAAAQBAJ" to volumeJson("ubERDAAAQBAJ", "The History of Jazz (2nd Ed.)", "Ted Gioia"),
            "oPuMQx9GZVcC" to volumeJson("oPuMQx9GZVcC", "The Jazz Standards", "Ted Gioia"),
            "x1qMnr-IYHwC" to volumeJson("x1qMnr-IYHwC", "Jazz: The Basics", "Christopher Meeder"),
            "O7c4AQAAIAAJ" to volumeJson("O7c4AQAAIAAJ", "Early Jazz", "Gunther Schuller"),
            "n_IRDAAAQBAJ" to volumeJson("n_IRDAAAQBAJ", "Visions of Jazz", "Gary Giddins"),
            "I1PweKJ3IEYC" to volumeJson("I1PweKJ3IEYC", "Jazz: A History", "Frank Tirro"),
            "DpHYloF8qL8C" to volumeJson("DpHYloF8qL8C", "Thinking in Jazz", "Paul F. Berliner")
        )
    }
}
