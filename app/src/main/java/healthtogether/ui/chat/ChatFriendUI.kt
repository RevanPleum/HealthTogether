package healthtogether.ui.chat

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.heathtogethermobile.R
import healthtogether.components.AppColor
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.friend.getDataFriendList
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.roundToInt


@Composable
fun ChatFriendView(
    navController: NavController,
    viewModel: HomeViewModel,
    message: MessageListViewModel,
    attachment: AttachmentsPickerViewModel,
    composer: MessageComposerViewModel,
    friendID: String
) = AppTheme {
    val focusManager = LocalFocusManager.current

    var friendData by remember {
        mutableStateOf(User())
    }

    LaunchedEffect(Unit) {
        friendData = getFriendData(friendID)[0]
    }



    Log.v("TestChannelLog", "Log : ${message.channel.messages}")

    Surface {
        Modifier.fillMaxSize()
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            TopAppNameAndProfile(navController, viewModel.state.imageProfile)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            Card(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxSize(),
                elevation = 20.dp,
                border = BorderStroke(0.dp, Color.White),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {

                        Spacer(Modifier.size(10.dp))

                        Image(
                            painterResource(id = R.drawable.iconback),
                            contentDescription = "Back Icon",
                            modifier = Modifier
                                .clickable {
                                    navController.popBackStack()
                                }
                                //.align(alignment = Alignment.CenterStart)
                                .padding(top = 30.dp, start = 10.dp))




                                val imageBytes = Base64.decode(friendData.image, Base64.DEFAULT)
                                val decodedImage = BitmapFactory.decodeByteArray(
                                    imageBytes,
                                    0,
                                    imageBytes.size
                                )

                                Spacer(Modifier.size(20.dp))

                                Image(
                                    painter = if (friendData.image.isNotEmpty()) rememberAsyncImagePainter(
                                        decodedImage
                                    ) else
                                        painterResource(id = R.drawable.profile),
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier.padding(top = 10.dp)
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .border(
                                            BorderStroke(1.dp, Color.White),
                                            CircleShape
                                        )
                                        ,
                                    contentScale = ContentScale.Crop,
                                )

                                Spacer(Modifier.size(20.dp))

                                Text(
                                    text = friendData.name,
                                    color = AppColor.Primary,
                                    textAlign = TextAlign.Start,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W600,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .padding(top = 30.dp)

                                )









                    }
                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(1.dp)
                            .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                    )

                    ChatTheme(
                        shapes = StreamShapes.defaultShapes().copy(
                            avatar = RoundedCornerShape(8.dp),
                            attachment = RoundedCornerShape(16.dp),
                            myMessageBubble = RoundedCornerShape(16.dp),
                            otherMessageBubble = RoundedCornerShape(16.dp),
                            inputField = RectangleShape
                        )
                    ) {
                        val isShowingAttachments = attachment.isShowingAttachments

                        Box(modifier = Modifier.fillMaxSize()) {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = {
                                    MessageComposer( // 3 - Add a composer
                                        composer,
                                        onAttachmentsClick = {
                                            attachment.changeAttachmentState(true)
                                        }
                                    )
                                }
                            ) {
                                MessageList( // 4 - Build the MessageList and connect the actions
                                    modifier = Modifier
                                        .background(ChatTheme.colors.appBackground)
                                        .padding(it)
                                        .fillMaxSize(),
                                    viewModel = message

                                )
                            }

                        }


                    }


                }
            }
        }
    }


}


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun getFriendData(friendID: String): List<User> {
    val request = QueryUsersRequest(
        filter = Filters.eq("id", friendID),
        offset = 0,
        limit = 10
    )

    return suspendCancellableCoroutine { continuation ->
        ChatClient.instance().queryUsers(request).enqueue() { result ->
            if (result.isSuccess)
                continuation.resume(result.data())
            else
                continuation.resumeWithException(Exception("Query Failed: ${result.error().message}"))
        }
    }
}