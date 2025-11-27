package healthtogether.ui.liveathome.livemodel.liveapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface VideoSdkApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJkN2NiMTQ0Yi04YzliLTQwMjMtYWI0Zi0zZjYyOTQ3ZGYyODciLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTc0NjEyODE3NiwiZXhwIjoxNzYxNjgwMTc2fQ.S2TitNQWEqoKwKqPaxNSASCyo1Oq-WAMwhXMQY8kmIQ" // 🔒 เปลี่ยนเป็น token จริง
    )
    @POST("v2/rooms")
    fun createRoom(
        @Body request: CreateRoomRequest
    ): Call<CreateRoomResponse>
}
