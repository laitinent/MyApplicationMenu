package fi.hamk.riksu.myapplication.building

data class Resource(
    val code: String,
    val description: String,
    val id: String,
    val name: String,
    val parent: String, // JExtendedResource
    val places: Int, // 0
    val resourceType: String,
    val type: String
)