package fi.hamk.riksu.myapplication.reservations

data class Resource(
    val code: String,
    val id: String,
    val name: String,
    val parent: String, // JResource
    val type: String
)