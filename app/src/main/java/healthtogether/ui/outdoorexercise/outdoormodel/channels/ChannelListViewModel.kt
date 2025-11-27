package healthtogether.ui.outdoorexercise.outdoormodel.channels

import android.util.Log
import androidx.annotation.DoNotInline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import healthtogether.apiService.model.Dropdowns.BaseDropdownViewModel
import healthtogether.apiService.model.Dropdowns.DropdownBodyViewModel
import healthtogether.apiService.service.DropdownService
import healthtogether.components.ExerciseAtHomeScreen
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.ui.liveathome.livemodel.LiveViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class ChannelListViewModel @Inject constructor(
    private val client: ChatClient,

) : ViewModel() {

//    private val _createChannelEvent = MutableSharedFlow<CreateChannelEvent>()
//    val createChannelEvent = _createChannelEvent.asSharedFlow()

    fun getUser(): ChatClient {
        return client
    }




    suspend fun createChannel(
        channelName: String,
        province: String,
        district: String,
        subDistrict: String,
        place: String,
        channelType: String = "messaging",
        exercise: String,
        gender: String,
        startTime: String,
        endTime: String,
        maxMember: String,
        lat: Double?,
        lng: Double?,
    ):String? {
        val channelId =
            province + district + subDistrict + place + channelName.replace(" ", "")



        return suspendCancellableCoroutine { continuation ->
            client.createChannel(
                channelType = channelType,
                channelId = channelId,
                memberIds = listOf(client.getCurrentUser()?.id) as List<String>,
                extraData = mapOf(
                    "name" to channelName.trim(),
                    "maxMember" to maxMember,
                    "exercise" to exercise,
                    "gender" to gender,
                    "time" to mapOf(
                        "timeStart" to startTime,
                        "timeEnd" to endTime
                    ),
                    "location" to mapOf(
                        "lat" to lat,
                        "lng" to lng
                    ),
                    "placeAddress" to province + district + subDistrict + place,
                    "is_public" to true

                )
            ).enqueue { result ->

                if (result.isSuccess) {
                    continuation.resume(result.data().cid)
                } else {
                    continuation.resume(null)
                }
            }

            // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
            continuation.invokeOnCancellation {
                // Clean up if needed
            }
        }

    }

    suspend fun createLiveChannel(
        channelName: String,
        channelType: String = "messaging",
        exercise: String,
        startTime: String,
        endTime: String,
        navController: NavController,
        liveViewModel: LiveViewModel
    ):String? {
        val channelId = channelName
        val liveID = liveViewModel.callCreateRoomApi(channelName)

        return suspendCancellableCoroutine { continuation ->

            client.createChannel(
                channelType = channelType,
                channelId = channelId,
                memberIds = listOf(client.getCurrentUser()?.id) as List<String>,
                extraData = mapOf(
                    "name" to channelName.trim(),
                    "exercise" to exercise,
                    "time" to mapOf(
                        "timeStart" to startTime,
                        "timeEnd" to endTime
                    ),
                    "liveID" to liveID
                )
            ).enqueue { result ->

                if (result.isSuccess) {
                    continuation.resume(result.data().cid)
                }

                else {
                    continuation.resume(null)
                }
            }

            // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
            continuation.invokeOnCancellation {
                // Clean up if needed
            }
        }

    }



    sealed class CreateChannelEvent {
        data class Error(val error: String) : CreateChannelEvent()
        object Success : CreateChannelEvent()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getChannel(selectPlaceID: String): List<Channel> {
        val request = QueryChannelsRequest(
            filter =


            if (selectPlaceID == "live"){
                Filters.and(
                    Filters.eq("type", "messaging"),
                    Filters.eq("disabled", false),
                    Filters.exists("liveID")
                )
            }

            else if (selectPlaceID == "none") {
                Filters.and(
                    Filters.eq("type", "messaging"),
                    Filters.`in`("members", getUser().getCurrentUser()!!.id),
                    Filters.exists("exercise")
                )
            }

            else {
                Filters.and(
                    Filters.eq("type", "messaging"),
                    Filters.eq("disabled", false),
                    Filters.autocomplete("placeAddress", selectPlaceID),
                )
            },
            offset = 0,
            limit = 50,
            //querySort = QuerySortByField.descByName("create_at")
        ).apply {
            watch = true
            state = true
        }




        return suspendCancellableCoroutine { continuation ->
            client.queryChannels(request).enqueue { result ->
                if (result.isSuccess) {
                    // Resume the coroutine with the result

                    continuation.resume(result.data())

                } else {
                    // If there's an error, resume with an exception
                    continuation.resumeWithException(Exception("Query failed: ${result.error().message}"))
                }
            }

            // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
            continuation.invokeOnCancellation {
                // Clean up if needed
            }
        }

    }


    fun setLocation(roomId: String, Lat: Double, Lng: Double) {
        client.updateChannelPartial("messaging", roomId, set = mapOf("location.lat" to Lat))
            .enqueue { result ->
                if (result.isSuccess) {
                    Log.v("testUpdateChannel", "location lat update : Success")
                } else {
                    Log.v("testUpdateChannel", "location lat update : fail")
                    Log.v("testUpdateChannel", "${result.error().message}")
                }
            }
        client.updateChannelPartial("messaging", roomId, set = mapOf("location.lng" to Lng))
            .enqueue { result ->
                if (result.isSuccess) {
                    Log.v("testUpdateChannel", "location lng update : Success")
                } else {
                    Log.v("testUpdateChannel", "location lng update : fail")
                    Log.v("testUpdateChannel", "${result.error().message}")
                }
            }
    }

//    suspend fun getProvinceList(language: String): List<BaseDropdownViewModel> {
//
//        return suspendCancellableCoroutine { continuation ->
//            viewModelScope.launch(Dispatchers.Default) {
//
//                val response = DropdownService.dropdown_API.province(language)
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccess!!) {
//                        continuation.resume(response.data!!)
//                    } else {
//                        continuation.resumeWithException(Exception("Query Failed: ${response.message}"))
//                    }
//                }
//            }
//
//            // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
//            continuation.invokeOnCancellation {
//                // Clean up if needed
//
//            }
//        }
//    }
//
//    suspend fun getDistrictList(provinceID: String, language: String): List<BaseDropdownViewModel> {
//
//        return suspendCancellableCoroutine { continuation ->
//            viewModelScope.launch(Dispatchers.Default) {
//
//                val response = DropdownService.dropdown_API.district(provinceID, language)
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccess!!) {
//                        continuation.resume(response.data!!)
//                    } else {
//                        continuation.resumeWithException(Exception("Query Failed: ${response.message}"))
//                    }
//                }
//            }
//
//            // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
//            continuation.invokeOnCancellation {
//                // Clean up if needed
//
//            }
//        }
//    }
//
//    suspend fun getSubDistrictList(
//        districtID: String,
//        language: String
//    ): List<BaseDropdownViewModel> {
//
//        return suspendCancellableCoroutine { continuation ->
//            viewModelScope.launch(Dispatchers.Default) {
//
//                val response = DropdownService.dropdown_API.subDistrict(districtID, language)
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccess!!) {
//                        continuation.resume(response.data!!)
//                    } else {
//                        continuation.resumeWithException(Exception("Query Failed: ${response.message}"))
//                    }
//                }
//            }
//
//            // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
//            continuation.invokeOnCancellation {
//                // Clean up if needed
//
//            }
//        }
//    }
//
//    suspend fun getPlace(
//        provinceID: String,
//        districtID: String,
//        subDistrictID: String,
//        language: String
//    ): List<BaseDropdownViewModel> {
//
//        return suspendCancellableCoroutine { continuation ->
//            viewModelScope.launch(Dispatchers.Default) {
//                val requestBody = DropdownBodyViewModel(provinceID, districtID, subDistrictID)
//                val response = DropdownService.dropdown_API.place(requestBody, language)
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccess!!) {
//                        continuation.resume(response.data!!)
//                    } else {
//                        continuation.resumeWithException(Exception("Query Failed: ${response.message}"))
//                    }
//                }
//            }
//
//            // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
//            continuation.invokeOnCancellation {
//                // Clean up if needed
//
//            }
//        }
//    }
//
//    suspend fun getExerciseList(
//        language: String
//    ): List<BaseDropdownViewModel> {
//
//        return suspendCancellableCoroutine { continuation ->
//            viewModelScope.launch(Dispatchers.Default) {
//
//                val response = DropdownService.dropdown_API.exercise(language)
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccess!!) {
//                        continuation.resume(response.data!!)
//                    } else {
//                        continuation.resumeWithException(Exception("Query Failed: ${response.message}"))
//                    }
//                }
//            }
//
//            // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
//            continuation.invokeOnCancellation {
//                // Clean up if needed
//
//            }
//        }
//    }
//
//    suspend fun getGenderList(
//        language: String
//    ): List<BaseDropdownViewModel> {
//
//        return suspendCancellableCoroutine { continuation ->
//            viewModelScope.launch(Dispatchers.Default) {
//
//                val response = DropdownService.dropdown_API.gender(language)
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccess!!) {
//                        continuation.resume(response.data!!)
//                    } else {
//                        continuation.resumeWithException(Exception("Query Failed: ${response.message}"))
//                    }
//                }
//            }
//
//            // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
//            continuation.invokeOnCancellation {
//                // Clean up if needed
//
//            }
//        }
//    }


}











