package fi.hamk.riksu.myapplication.reservations

data class Reservation(
    val description: String,
    val endDate: String,
    val id: String,
    val modifiedDate: String,
    val resources: List<Resource>,
    val startDate: String,
    val subject: String
)