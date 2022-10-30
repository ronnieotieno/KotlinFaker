package models


data class Image(
    val imageId: Int = 0,
    val comments: Int,
    val downloads: Int,
    val id: Int,
    val largeImageURL: String,
    val likes: Int,
    val tags: String,
    val user: String,
    val user_id: Int,
    val views: Int,
    val users: List<String>,
    val userIds: List<Int>,
    val shorts: List<Short>,
    val longs: List<Long>,
    val booleans: List<Boolean>,
    val chars: List<Char>,
    val char: Char,
    val double: Double,
    val doubles: List<Double>,
    val float: Float,
    val floats: List<Float>,
    var searchTerm: String? = null
)