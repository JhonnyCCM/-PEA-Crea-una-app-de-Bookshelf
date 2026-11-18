package com.example.bookshelf.model

import com.google.gson.annotations.SerializedName

/**
 * Response returned by the Google Books search endpoint (`volumes?q=...`).
 */
data class BookSearchResponse(
    @SerializedName("items")
    val items: List<BookIdItem> = emptyList()
)

/**
 * Lightweight item containing the Google Books volume ID from search results.
 */
data class BookIdItem(
    @SerializedName("id")
    val id: String
)

/**
 * Detailed volume returned by the Google Books volume detail endpoint (`volumes/{id}`).
 */
data class BookVolume(
    @SerializedName("id")
    val id: String,
    @SerializedName("volumeInfo")
    val volumeInfo: VolumeInfo
)

/**
 * Metadata associated with a book volume.
 */
data class VolumeInfo(
    @SerializedName("title")
    val title: String,
    @SerializedName("authors")
    val authors: List<String>? = null,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks? = null
)

/**
 * Thumbnail URLs provided by Google Books API.
 * Replaces http:// with https:// so Android clears cleartext traffic checks.
 */
data class ImageLinks(
    @SerializedName("smallThumbnail")
    val smallThumbnail: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String
) {
    val httpsThumbnail: String
        get() = thumbnail.replace("http://", "https://")
}
