package fi.hamk.riksu.myapplication.reservations

data class Reservations(
    val message: String,
    val reservations: List<Reservation>,
    val status: String
)