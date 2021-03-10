package fi.hamk.riksu.myapplication.building

data class BuildingData(
    val building: Building,
    val message: String,
    val resources: List<Resource>,
    val status: String
)