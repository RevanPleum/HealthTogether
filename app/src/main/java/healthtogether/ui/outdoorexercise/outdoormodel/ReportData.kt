package healthtogether.ui.outdoorexercise.outdoormodel

import kotlinx.serialization.Serializable

@Serializable
data class ReportData(
    val roomID : String,
    val userDoWrongID : String,
    val complaintType : String,
    val moreInformation : String,
    val imageInfo : String,
    //val userIDReport : String
)
