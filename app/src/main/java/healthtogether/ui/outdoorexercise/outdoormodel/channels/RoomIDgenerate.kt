package healthtogether.ui.outdoorexercise.outdoormodel.channels

fun createRoomID(province: String, district: String, subDistrict: String, place: String): String {
    return when (province) {
        "Bangkok" -> "00"
        "Patumtanee" -> "01"
        "Rayong" -> "02"
        else -> ""
    } +

            when (district) {
                "DistrictTest1" -> "00"
                "DistrictTest2" -> "01"
                else -> ""
            } +

            when (subDistrict) {
                "SubDistrictTest1" -> "00"
                else -> ""
            } +

            when (place) {
                "Ramkhamhaeng University" -> "00"
                "Rajamangala Sta" -> "01"
                else -> ""
            }

}