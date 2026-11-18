package com.example.bookshelf.fake

import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.model.BookVolume

class FakeNetworkBookshelfRepository : BookshelfRepository {
    override suspend fun getBooks(query: String): List<BookVolume> {
        return FakeDataSource.booksList
    }
}
