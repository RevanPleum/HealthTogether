package healthtogether.ui.liveathome.livemodel.liveapi

data class CreateRoomResponse(
    val roomId: String?,
    val customRoomId: String?,
    val userId: String?,
    val disabled: Boolean?,
    val createdAt: String?,
    val updateAt: String?,
    val id: String?,
    val links: Links?

)

data class Links(
    val get_room: String?,
    val get_session: String?
)
