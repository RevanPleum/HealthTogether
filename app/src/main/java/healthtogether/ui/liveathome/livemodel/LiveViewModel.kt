package healthtogether.ui.liveathome.livemodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import healthtogether.ui.liveathome.StreamingMode
import healthtogether.ui.liveathome.livemodel.liveapi.CreateRoomRequest
import healthtogether.ui.liveathome.livemodel.liveapi.CreateRoomResponse
import healthtogether.ui.liveathome.livemodel.liveapi.RetrofitClient
import kotlinx.coroutines.suspendCancellableCoroutine
import live.videosdk.rtc.android.Meeting
import live.videosdk.rtc.android.Participant
import live.videosdk.rtc.android.VideoSDK
import live.videosdk.rtc.android.listeners.MeetingEventListener
import retrofit2.Call
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LiveViewModel: ViewModel() {
    private var stream: Meeting? = null
    private var micEnabled by mutableStateOf(true)
    private var webcamEnabled by mutableStateOf(true)
    val currentParticipants = mutableStateListOf<Participant>()
    var fullScreenMode by mutableStateOf(false)

    var localParticipantId by mutableStateOf("")
        private set

    var streamEnd by mutableStateOf(false)

    var isStreamLeft by mutableStateOf(false)
        private set


    var isJoined by mutableStateOf(false)
        private set

    fun initStream(context: Context, token: String, streamId: String, mode: StreamingMode) {
        VideoSDK.config(token)
        if (stream == null) {
            stream =
                VideoSDK.initMeeting(
                    context,streamId, null,
                    micEnabled, webcamEnabled, null, mode.name , true, null, null
                )
        }

        stream!!.addEventListener(meetingEventListener)
        stream!!.join()
        isJoined = true

    }

    private val meetingEventListener: MeetingEventListener = object : MeetingEventListener() {
        override fun onMeetingJoined() {
            stream?.let {
                if (it.localParticipant.mode != "RECV_ONLY") {
                    if (!currentParticipants.contains(it.localParticipant)) {
                        currentParticipants.add(it.localParticipant)
                    }
                }
                localParticipantId = it.localParticipant.id
            }
        }

        override fun onMeetingLeft() {
            currentParticipants.clear()
            stream = null
            isStreamLeft = true
            isJoined = false
            fullScreenMode = false
        }

        override fun onParticipantJoined(participant: Participant) {
            if (participant.mode != "RECV_ONLY") {
                currentParticipants.add(participant)
            }

        }

        override fun onParticipantLeft(participant: Participant) {
                currentParticipants.remove(participant)


        }




    }

    fun leaveStream() {
        currentParticipants.clear()
        stream?.end()
        stream?.removeAllListeners()
        stream = null
        isStreamLeft = true
        isJoined = false
        fullScreenMode = false
        Log.v("TestLive", "Leave")

    }


    fun setLeaveLogic(){
        isStreamLeft = false
    }

    fun toggleMode() {
        fullScreenMode = !fullScreenMode
    }

    fun toggleWebcam() {
        if (webcamEnabled) stream?.disableWebcam() else stream?.enableWebcam()
        webcamEnabled = !webcamEnabled
    }


    suspend fun callCreateRoomApi(roomID: String): String {
        val request = CreateRoomRequest(
            customRoomId = roomID,
            webhook = null,
            autoCloseConfig = null,
            autoStartConfig = null
        )

        val call = RetrofitClient.instance.createRoom(request)

        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : retrofit2.Callback<CreateRoomResponse> {
                override fun onResponse(
                    call: Call<CreateRoomResponse>,
                    response: retrofit2.Response<CreateRoomResponse>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body()?.roomId.toString())
                    } else {
                        continuation.resumeWithException(Exception("response failed: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<CreateRoomResponse>, t: Throwable) {
                    Log.v("TestCreateLive","API call failed: ${t.message}")
                }
            })
        }
    }


}