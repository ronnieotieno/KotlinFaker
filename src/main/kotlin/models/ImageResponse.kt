package models

data class ImageResponse(
    val images: List<Image>,
    val total: Int,
    val totalHits: Int,
)