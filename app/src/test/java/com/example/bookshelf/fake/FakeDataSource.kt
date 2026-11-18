package com.example.bookshelf.fake

import com.example.bookshelf.model.BookIdItem
import com.example.bookshelf.model.BookSearchResponse
import com.example.bookshelf.model.BookVolume
import com.example.bookshelf.model.ImageLinks
import com.example.bookshelf.model.VolumeInfo

object FakeDataSource {
    const val idOne = "book1"
    const val idTwo = "book2"
    const val titleOne = "The History of Jazz"
    const val titleTwo = "Early Jazz"
    const val thumbOne = "http://books.google.com/books/content?id=book1&printsec=frontcover&img=1&zoom=1"
    const val thumbTwo = "http://books.google.com/books/content?id=book2&printsec=frontcover&img=1&zoom=1"

    val searchResponse = BookSearchResponse(
        items = listOf(
            BookIdItem(id = idOne),
            BookIdItem(id = idTwo)
        )
    )

    val booksList = listOf(
        BookVolume(
            id = idOne,
            volumeInfo = VolumeInfo(
                title = titleOne,
                authors = listOf("Ted Gioia"),
                imageLinks = ImageLinks(thumbnail = thumbOne)
            )
        ),
        BookVolume(
            id = idTwo,
            volumeInfo = VolumeInfo(
                title = titleTwo,
                authors = listOf("Gunther Schuller"),
                imageLinks = ImageLinks(thumbnail = thumbTwo)
            )
        )
    )
}
