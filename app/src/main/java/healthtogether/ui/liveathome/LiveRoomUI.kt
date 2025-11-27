package healthtogether.ui.liveathome

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.components.AppColor
import healthtogether.components.ExerciseAtHomeScreen
import healthtogether.components.MyAppButton
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.liveathome.livemodel.LiveViewModel
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


@Composable
fun LiveRoomView(
    navController: NavController,
    viewModel: HomeViewModel,
    channel: ChannelListViewModel,
    message: MessageListViewModel,
    attachment: AttachmentsPickerViewModel,
    composer: MessageComposerViewModel,
    liveViewModel: LiveViewModel,
    context: Context
) = AppTheme {


    var userMode by remember {
        mutableStateOf(StreamingMode.RECV_ONLY)
    }

    val liveToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJmZjZmNmI5OS0yMjBjLTQ4MzAtYmNmMy05YmU0MjZhY2VkNjAiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTc1MzM3NTc4MCwiZXhwIjoxOTExMTYzNzgwfQ.PWllvfxk8Jy6IaMIe902i06jQjz-kZPZelV38t7WCmk"



    val lifecycleOwner = LocalLifecycleOwner.current

    // ใช้ State แยกเพื่อรอให้ทุกอย่างพร้อมก่อน trigger
    val autoJoin = remember { mutableStateOf(false) }

    if(message.channel.createdBy.id == message.chatClient.getCurrentUser()!!.id)
        userMode = StreamingMode.SEND_AND_RECV

    // Observer รอให้ Activity อยู่ใน ON_RESUME แล้วจึงตั้งค่าว่าพร้อม
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && !liveViewModel.isJoined && userMode.name == StreamingMode.RECV_ONLY.name) {
                autoJoin.value = true
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(liveViewModel.isStreamLeft) {
        if (liveViewModel.isStreamLeft) {
            navController.popBackStack()
            liveViewModel.setLeaveLogic()
            navController.navigate(ExerciseAtHomeScreen.RatingTrainer.routeWithGroupRoomId(message.channel.cid))
        }
    }


    LaunchedEffect(autoJoin.value) {
        if (autoJoin.value && !liveViewModel.isJoined) {
            liveViewModel.initStream(context, liveToken, message.channel.extraData["liveID"].toString(), userMode)
        }
    }

    Surface {
        Modifier.fillMaxSize()
    }
    Column {

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
                Column(Modifier.padding(top = 20.dp)) {

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painterResource(id = R.drawable.iconback),
                            contentDescription = "Back Icon",
                            modifier = Modifier
                                .clickable {

                                    if (channel.getUser().getCurrentUser()?.id == message.channel.createdBy.id)
                                        liveViewModel.leaveStream()

                                        channel.getUser().removeMembers(
                                            "messaging", message.channel.id,
                                            listOf(channel.getUser().getCurrentUser()!!.id)
                                        ).enqueue { result ->
                                            if (result.isSuccess) {
                                                Log.v("TestRemove", "Success")
                                                navController.popBackStack()
                                            } else {
                                                Log.v(
                                                    "TestRemove",
                                                    "fail : ${result.error().message}"
                                                )
                                            }
                                        }

                                }
                                //.align(alignment = Alignment.CenterStart)
                                .padding(top = 5.dp, start = 10.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                //.padding(top = 5.dp)
                            )
                            {
                                Text(
                                    text = "Title : ",
                                    color = AppColor.Primary,
                                    fontWeight = FontWeight.W700,
                                    fontSize = 20.sp,

                                )

                                Text(
                                    text = message.channel.name,
                                    color = AppColor.Primary,
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W400,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )



                            }




                        }

                    }

                    Spacer(Modifier.size(20.dp))

                    Card(
                            backgroundColor = if(!liveViewModel.fullScreenMode) AppColor.Primary else Color.White,
                            modifier = Modifier.fillMaxWidth()
                                .height(40.dp)
                                .padding(start = 10.dp, end = 10.dp),
                            shape = RoundedCornerShape(
                                topStart = 30.dp,
                                topEnd = 30.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 0.dp
                            ),
                        elevation = if(liveViewModel.fullScreenMode) 0.dp else 1.dp) {

                        Row (horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                            ){

                            Text(text = message.channel.createdBy.name,
                                color = if(liveViewModel.fullScreenMode) AppColor.Primary else Color.White,
                                fontWeight = FontWeight.W700,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 20.dp))


                            if(!liveViewModel.fullScreenMode)
                            IconButton(onClick = {
                                navController.navigate(
                                    OutdoorExerciseScreen.ReportGroup.routeWithGroupRoomId(message.channel.cid)
                                )
                            }) {
                                Row {
                                    Image(painter = painterResource(id = R.drawable.icon_report)
                                        , contentDescription = "Report Icon",
                                        modifier = Modifier.padding(end = 5.dp))

                                    Text("Report",
                                        color = Color.White,
                                        fontWeight = FontWeight.W700,
                                        fontSize = 18.sp,
                                        textDecoration = TextDecoration.Underline,
                                    )
                                }

                            }


                             }

                        }

                    StreamingScreen(viewModel = liveViewModel, streamId = message.channel.extraData["liveID"].toString(), liveToken = liveToken,
                        mode = userMode,
                        context = context, fullScreenMode = liveViewModel.fullScreenMode)

                    //อย่่าเขียนifครอบไว้ทั้งหมด เพราะหน้าจอไลฟ์(StreamingScreen)มันปรับขนาดไม่ทัน
                        Card(
                            backgroundColor = if (!liveViewModel.fullScreenMode) AppColor.Primary else Color.White,
                            modifier = Modifier.fillMaxWidth()
                                .height(60.dp)
                                .padding(start = 10.dp, end = 10.dp),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomEnd = 30.dp,
                                bottomStart = 30.dp
                            ),
                            elevation = if(liveViewModel.fullScreenMode) 0.dp else 1.dp
                        ) {

                            MediaControlButtons(
                                onJoinClick = { liveViewModel.initStream(context, liveToken, message.channel.extraData["liveID"].toString(), userMode)},
                                onModeToggleClick = { liveViewModel.toggleMode() },
                                onLeaveClick = { liveViewModel.leaveStream()
                                               },
                                fullScreenMode = liveViewModel.fullScreenMode,
                                isJoined = liveViewModel.isJoined,
                                onCamClick = { liveViewModel.toggleWebcam()},
                                userMode = userMode,
                                viewerCount = message.channel.memberCount.toString(),
                                navController = navController,
                                channelID = message.channel.cid
                            )

                        }

                        Spacer(Modifier.size(10.dp))

                    if (!liveViewModel.fullScreenMode) {
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

                                    Log.v("TestChannelLog", "Log : ${message.channel.messages}")
                                }

                            }


                        }
                    }
                    }
                }
            }
        }
    }





@Composable
fun MediaControlButtons(
    onJoinClick: () -> Unit,
    onModeToggleClick: () -> Unit,
    onLeaveClick: () -> Unit,
    fullScreenMode: Boolean,
    isJoined: Boolean,
    onCamClick: () -> Unit,
    userMode: StreamingMode,
    viewerCount: String,
    navController: NavController,
    channelID: String
) {
    Column( modifier = Modifier
        .fillMaxWidth()
        .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        Row( modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isJoined && userMode.name != StreamingMode.RECV_ONLY.name) {
                IconButton(
                    onClick = {
                        navController.navigate(OutdoorExerciseScreen.MemberList.routeWithGroupRoomId(channelID))
                    }) {
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(id = R.drawable.iconviewer),
                        contentDescription = "Viewer Icon",
                        Modifier.padding(end = 3.dp)
                            .size(20.dp))

                        Text(text = viewerCount,
                            color = if (fullScreenMode) AppColor.Primary else Color.White,
                            fontWeight = FontWeight.W700,
                            fontSize = 18.sp,) }

                }

                MyAppButton(onJoinClick, "Start Live")

                IconButton(
                    onClick = onModeToggleClick,
                    Modifier.padding(end = 10.dp)
                ) {
                    Icon(painter = painterResource(id = if (fullScreenMode) R.drawable.iconwindowsscreen else R.drawable.iconfullscreen),
                        contentDescription = "Toggle Icon",
                        tint = if (fullScreenMode) AppColor.Primary else Color.White,
                        modifier = Modifier.size(if (!fullScreenMode) 20.dp else 30.dp)
                        )

                }
            }

            else if(userMode.name != StreamingMode.RECV_ONLY.name){
                IconButton(
                    onClick = {
                        navController.navigate(OutdoorExerciseScreen.MemberList.routeWithGroupRoomId(channelID))
                    }) {
                    Row (horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(id = R.drawable.iconviewer),
                            contentDescription = "Viewer Icon",
                            Modifier.padding(end = 3.dp)
                                .size(20.dp))

                        Text(text = viewerCount,
                            color = if (fullScreenMode) AppColor.Primary else Color.White) }

                }

                MyAppButton(
                    task = onCamClick,
                    buttonName = "Toggle Cam"
                )

                MyAppButton(
                    task = onLeaveClick,
                    buttonName = "End Live"
                )

                IconButton(
                    onClick = onModeToggleClick) {
                    Icon(painter = painterResource(id = if (fullScreenMode) R.drawable.iconwindowsscreen else R.drawable.iconfullscreen),
                        contentDescription = "Toggle Icon",
                        tint = if (fullScreenMode) AppColor.Primary else Color.White,
                                modifier = Modifier.size(if (!fullScreenMode) 20.dp else 30.dp)
                    )

                }
            }

            else {
                IconButton(
                    onClick = {
                        navController.navigate(OutdoorExerciseScreen.MemberList.routeWithGroupRoomId(channelID))
                    }) {
                    Row (horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(id = R.drawable.iconviewer),
                            contentDescription = "Viewer Icon",
                            Modifier.padding(end = 3.dp)
                                .size(20.dp))

                        Text(text = viewerCount,
                            color = if (fullScreenMode) AppColor.Primary else Color.White) }

                }



                IconButton(
                    onClick = onModeToggleClick) {
                    Icon(painter = painterResource(id = if (fullScreenMode) R.drawable.iconwindowsscreen else R.drawable.iconfullscreen),
                        contentDescription = "Toggle Icon",
                        tint = if (fullScreenMode) AppColor.Primary else Color.White,
                        modifier = Modifier.size(if (!fullScreenMode) 20.dp else 30.dp)
                    )

                }

            }

        }
    }
}