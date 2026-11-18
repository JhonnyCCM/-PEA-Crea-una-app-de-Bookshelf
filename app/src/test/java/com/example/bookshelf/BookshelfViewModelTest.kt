package com.example.bookshelf

import com.example.bookshelf.fake.FakeDataSource
import com.example.bookshelf.fake.FakeNetworkBookshelfRepository
import com.example.bookshelf.rules.TestDispatcherRule
import com.example.bookshelf.ui.screens.BookshelfUiState
import com.example.bookshelf.ui.screens.BookshelfViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookshelfViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun bookshelfViewModel_getBooks_verifyBookshelfUiStateSuccess() {
        val bookshelfViewModel = BookshelfViewModel(
            bookshelfRepository = FakeNetworkBookshelfRepository()
        )
        assertEquals(
            BookshelfUiState.Success(FakeDataSource.booksList),
            bookshelfViewModel.bookshelfUiState
        )
    }
}
