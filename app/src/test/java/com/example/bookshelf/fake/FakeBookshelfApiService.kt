package com.example.bookshelf.fake

import com.example.bookshelf.model.BookSearchResponse
import com.example.bookshelf.model.BookVolume
import com.example.bookshelf.network.BookshelfApiService

class FakeBookshelfApiService : BookshelfApiService {
    override suspend fun searchBooks(query: String): BookSearchResponse {
        return FakeDataSource.searchResponse
    }

    override suspend fun getBook(id: String): BookVolume {
        return FakeDataSource.booksList.first { it.id == id }
    }
}
