package healthtogether.ui.liveathome.livemodel.liveapi

data class CreateRoomRequest(
    val customRoomId: String? = null,
    val webhook: String? = null,
    val autoCloseConfig: String? = null,
    val autoStartConfig: String? = null
)