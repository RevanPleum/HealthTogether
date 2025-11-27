package healthtogether.ui.outdoorexercise.outdoormodel

import kotlinx.serialization.Serializable

@Serializable
data class OutdoorRoomSearchData(
    val province: String = "",
    val district: String = "",
    val subDistrict: String = "",
    val placeValue: String = "",
    val placeName: String = "",
    val roomName: String = "",
    val exercise: String = "",
    val gender: String = ""
)
