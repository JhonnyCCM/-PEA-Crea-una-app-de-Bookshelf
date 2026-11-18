package com.example.bookshelf

import com.example.bookshelf.data.NetworkBookshelfRepository
import com.example.bookshelf.fake.FakeBookshelfApiService
import com.example.bookshelf.fake.FakeDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkBookshelfRepositoryTest {
    @Test
    fun networkBookshelfRepository_getBooks_verifyBookList() = runTest {
        val repository = NetworkBookshelfRepository(
            bookshelfApiService = FakeBookshelfApiService()
        )
        assertEquals(
            FakeDataSource.booksList,
            repository.getBooks("jazz history")
        )
    }

    @Test
    fun imageLinks_httpsThumbnail_convertsHttpToHttps() {
        val book = FakeDataSource.booksList.first()
        val httpsUrl = book.volumeInfo.imageLinks?.httpsThumbnail
        assertEquals(
            "https://books.google.com/books/content?id=book1&printsec=frontcover&img=1&zoom=1",
            httpsUrl
        )
    }
}
