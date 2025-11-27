package healthtogether.ui.home.homemodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import healthtogether.ui.login.loginmodel.CreateToken
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {

    var state by mutableStateOf(HomeViewState())
        private set


    fun TestQuickLogin(userName: String, tokenChat: String) {
        val userID = "Pa001"
        val user = User(userID)
        val jwt1 = CreateToken(
            "Pa001",
            "bdcm9uc3thmtsa8kxeujehtctfmksa4hc8rmns85wze3vjcmbyux7evt2b426gh5"
        )

        Log.v("TestToken", "${userID} : $jwt1")

        client.connectUser(
            user = user,
            token = jwt1
        ).enqueue { result ->
            if (result.isSuccess) {
                Log.v("TestQuickLogin", "login: Success")

                var checkExerciseEmptyList =
                    result.data().user.extraData.get("exercise").toString().removePrefix("[")
                        .removeSuffix("]").split(",").map { it.trim() }
                if (checkExerciseEmptyList[0] == "")
                    checkExerciseEmptyList = emptyList()

                var checkPlaceEmptyList =
                    result.data().user.extraData.get("place").toString().removePrefix("[")
                        .removeSuffix("]").split(",").map { it.trim() }
                if (checkPlaceEmptyList[0] == "")
                    checkPlaceEmptyList = emptyList()

                state = state.copy(
                    userID = result.data().user.id,
                    userName = result.data().user.name,
                    imageProfile = result.data().user.image,
                    userExerciseFav = checkExerciseEmptyList,
                    userPlaceFav = checkPlaceEmptyList,
                    userGender = result.data().user.extraData.get("gender").toString(),
                    userAge = result.data().user.extraData.get("age").toString().toDouble()
                        .roundToInt(),
                    role = result.data().user.extraData.get("status").toString()

                )


            } else {
                Log.v("TestQuickLogin", "login: Failure")
                Log.v("TestQuickLogin", "${result.error().message}")

            }
        }


    }

    suspend fun RealLogin(userName: String, tokenChat: String) {
        val userID = userName
        val user = User(userID)

        return suspendCancellableCoroutine {

            client.connectUser(
                user = user,
                token = tokenChat
            ).enqueue { result ->
                if (result.isSuccess) {
                    state = state.copy(userID = client.getCurrentUser()!!.id)

                    it.resume(Unit)

                } else {
                    it.resumeWithException(Exception("Login failed: ${result.error().message}"))

                }
            }
        }



    }

    fun setProfileData(user: User) {

        var checkExerciseEmptyList =
            user.extraData.get("exercise").toString().removePrefix("[")
                .removeSuffix("]").split(",").map { it.trim() }
        if (checkExerciseEmptyList[0] == "")
            checkExerciseEmptyList = emptyList()

        var checkPlaceEmptyList =
            user.extraData.get("place").toString().removePrefix("[")
                .removeSuffix("]").split(",").map { it.trim() }
        if (checkPlaceEmptyList[0] == "")
            checkPlaceEmptyList = emptyList()

        state = state.copy(
            userID = user.id,
            userName = user.name,
            imageProfile = user.image,
            userExerciseFav = checkExerciseEmptyList,
            userPlaceFav = checkPlaceEmptyList,
            userGender = user.extraData.get("gender").toString(),
            userAge = user.extraData.get("age").toString().toDouble()
                .roundToInt(),
            role = user.extraData.get("status").toString())


    }

    fun TestUpdateFriend() {
        client.partialUpdateUser(
            state.userID,
            mapOf("friend_list" to listOf(""))
        ).enqueue { result ->
            if (result.isSuccess) {
                Log.v("UpdateProfile", "setImage: Success")
            } else {
                Log.v("UpdateProfile", "setImage: Error")
                Log.v("UpdateProfile", "setImage: ${result.error().message}")
            }
        }

        client.partialUpdateUser(
            state.userID,
            mapOf("friend_request" to listOf(""))
        ).enqueue { result ->
            if (result.isSuccess) {
                Log.v("UpdateProfile", "setImage: Success")
            } else {
                Log.v("UpdateProfile", "setImage: Error")
                Log.v("UpdateProfile", "setImage: ${result.error().message}")
            }
        }
    }



    suspend fun updateProfile(
        imageProfile: String,
        userName: String,
        userAge: Int,
        userGender: String,
        userFavExercise: List<String>,
        userFavPlace: List<String>,
    ) {
        state = state.copy(
            imageProfile = imageProfile,
            userName = userName,
            userAge = userAge,
            userGender = userGender,
            userExerciseFav = userFavExercise,
            userPlaceFav = userFavPlace
        )

        return suspendCancellableCoroutine { test ->

            client.partialUpdateUser(
                state.userID,
                mapOf("image" to state.imageProfile,
                    "name" to state.userName,
                    "exercise" to "${state.userExerciseFav.toList()}",
                    "place" to "${state.userPlaceFav.toList()}",
                    "age" to "${state.userAge}",
                    "gender" to state.userGender,
                    "status" to state.role
                    )
            ).enqueue { result ->

                if (result.isSuccess) {
                    test.resume(Unit)

                } else {
                    test.resumeWithException(Exception("Query failed: ${result.error().message}"))
                }

            }


        }
    }

    suspend fun setNewUserData(
        userID:String,
        exercise:String,
        place:String,
        age:String,
        gender:String,
        status:String,
        friendList:String,
        friendRequest:String
    ) {

        return suspendCancellableCoroutine { test ->

            client.partialUpdateUser(
                userID,
                mapOf(
                    "name" to "UnName",
                    "exercise" to listOf(exercise),
                    "place" to listOf(place),
                    "age" to age,
                    "gender" to gender,
                    "status" to status,
                    "friend_list" to listOf(friendList),
                    "friend_request" to listOf(friendRequest)
                )
            ).enqueue { result ->

                if (result.isSuccess) {
                    test.resume(Unit)

                } else {
                    test.resumeWithException(Exception("Query failed: ${result.error().message}"))
                }

            }


        }
    }

    fun Logout(){
        client.disconnect()
    }
}



