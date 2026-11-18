package com.example.bookshelf.data

import com.example.bookshelf.model.BookVolume
import com.example.bookshelf.network.BookshelfApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Repository contract that abstracts fetching books from a data source.
 */
interface BookshelfRepository {
    suspend fun getBooks(query: String = "jazz history"): List<BookVolume>
}

/**
 * Network implementation of [BookshelfRepository] backed by [BookshelfApiService].
 * Fetches matching volume IDs and then concurrently retrieves detailed metadata
 * and cover thumbnails for each book.
 */
class NetworkBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookshelfRepository {
    override suspend fun getBooks(query: String): List<BookVolume> {
        val searchResponse = bookshelfApiService.searchBooks(query)
        return coroutineScope {
            searchResponse.items.map { item ->
                async { bookshelfApiService.getBook(item.id) }
            }.awaitAll()
        }
    }
}
