package healthtogether.ui.outdoorexercise

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.heathtogethermobile.R
import healthtogether.components.AppColor
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.ChatEventListener
import io.getstream.chat.android.client.events.MemberRemovedEvent
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.Member
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.subscribeFor
import io.getstream.chat.android.client.utils.observable.Disposable
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import kotlin.math.roundToInt


@Composable
fun GroupRoomView(
    navController: NavController,
    viewModel: HomeViewModel,
    channel: ChannelListViewModel,
    message: MessageListViewModel,
    attachment: AttachmentsPickerViewModel,
    composer: MessageComposerViewModel
) = AppTheme {

    var exitRoom by remember { mutableStateOf(false) }

    var disposable: Disposable = message.chatClient.subscribeFor<MemberRemovedEvent>{
        if(it.user.id == message.chatClient.getCurrentUser()!!.id) {
            exitRoom = true
        }

    }

    LaunchedEffect(exitRoom) {
        if(exitRoom)
        {
            disposable.dispose()
            navController.popBackStack(OutdoorExerciseScreen.GroupRoom.route,true)
        }

    }


    var memberCount by remember { mutableStateOf(message.channel.memberCount) }



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
                        Image(
                            painterResource(id = R.drawable.iconback),
                            contentDescription = "Back Icon",
                            modifier = Modifier
                                .clickable {
                                    exitRoom = true
                                }
                                //.align(alignment = Alignment.CenterStart)
                                .padding(top = 20.dp, start = 10.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, top = 7.dp, bottom = 7.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                //.padding(top = 5.dp)
                            )
                            {
                                Text(
                                    text = "Name : ",
                                    color = AppColor.Primary,
                                    fontWeight = FontWeight.W700,
                                    fontSize = 18.sp,
                                )

                                Text(
                                    text = "${message.channel.name}",
                                    color = AppColor.Primary,
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W400,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )



                                Text(
                                    text = "$memberCount/${
                                        message.channel.extraData.get(
                                            "maxMember"
                                        ).toString().toDoubleOrNull()?.roundToInt()
                                    }",
                                    color = AppColor.Primary,
                                    textAlign = TextAlign.End,
                                    fontWeight = FontWeight.W700,
                                    fontSize = 18.sp,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 20.dp)
                                        .clickable {
                                            navController.navigate(
                                                OutdoorExerciseScreen.MemberList.routeWithGroupRoomId(
                                                    message.channel.cid
                                                )
                                            )
                                        }
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                //.padding(top = 5.dp)
                            )
                            {
                                Text(
                                    text = "Category : ",
                                    color = AppColor.Primary,
                                    fontWeight = FontWeight.W700,
                                    fontSize = 18.sp,
                                )

                                Text(
                                    text = "${message.channel.extraData.get("exercise")}",
                                    color = AppColor.Primary,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.W400,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )


                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                //.padding(top = 5.dp)
                            )
                            {
                                Text(
                                    text = "Time : ",
                                    color = AppColor.Primary,
                                    fontWeight = FontWeight.W700,
                                    fontSize = 18.sp,
                                )


                                //channel.getUser().updateChannelPartial("messaging",roomId, set = mapOf("time.timeStart" to "18:35")).enqueue()
                                //channel.getUser().updateChannelPartial("messaging",roomId, set = mapOf("time.timeEnd" to "21:00")).enqueue()

                                val roomTime =
                                    message.channel.extraData.get("time") as? Map<String, Any>
                                Text(
                                    text = "${roomTime?.get("timeStart")} - ${roomTime?.get("timeEnd")}",
                                    color = AppColor.Primary,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.W400,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )

                                Image(
                                    painterResource(id = R.drawable.iconlocation),
                                    contentDescription = "Location Icon",
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate(
                                                OutdoorExerciseScreen.EditMarkerLocation.routeWithGroupRoomId(
                                                    message.channel.id
                                                )
                                            )
                                        }
                                        .align(Alignment.TopEnd)
                                        .padding(end = 35.dp),

                                    )

                            }

                            Box(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painterResource(id = R.drawable.icon_report),
                                    contentDescription = "Report Icon",
                                    modifier = Modifier
                                        .clickable {
                                            channel.getUser().updateChannelPartial(
                                                message.channel.type,
                                                message.channel.id,
                                                set = mapOf("frozen" to true)
                                            ).enqueue { result ->
                                                if (result.isSuccess) {
                                                    Log.v("testDis", result.isSuccess.toString())
                                                } else {
                                                    Log.v(
                                                        "testDis",
                                                        result.error().message.toString()
                                                    )
                                                }
                                            }
                                            //navController.navigate(OutdoorExerciseScreen.ReportGroup.routeWithGroupRoomId(message.channel.cid))
                                        }
                                        .align(Alignment.CenterStart)
                                        .padding(top = 5.dp),
                                )

                                Text(
                                    text = "Report",
                                    color = AppColor.Primary,
                                    fontWeight = FontWeight.W700,
                                    fontSize = 18.sp,
                                    textDecoration = TextDecoration.Underline,

                                    modifier = Modifier
                                        .padding(start = 30.dp, top = 12.dp)
                                        .clickable {
                                            navController.navigate(
                                                OutdoorExerciseScreen.ReportGroup.routeWithGroupRoomId(
                                                    message.channel.cid
                                                )
                                            )
                                        }
                                )

                                Button(colors = ButtonDefaults.buttonColors(
                                    backgroundColor = AppColor.Danger
                                ),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .height(40.dp)
                                        .width(95.dp)
                                        .padding(end = 15.dp, top = 10.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    onClick = {
                                        if (channel.getUser().getCurrentUser()?.id != null)
                                            channel.getUser().removeMembers(
                                                "messaging", message.channel.id,
                                                listOf(channel.getUser().getCurrentUser()!!.id)
                                            ).enqueue { result ->
                                                if (result.isSuccess) {
                                                    Log.v("TestRemove", "Success")
                                                    exitRoom = true

                                                } else {
                                                    Log.v(
                                                        "TestRemove",
                                                        "fail : ${result.error().message}"
                                                    )
                                                }
                                            }

                                    }) {
                                    Text(
                                        text = "EXIT",
                                        color = Color.White,
                                        fontWeight = FontWeight.W700,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }


                        }

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


//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                    ) {
//                        Card(
//                            backgroundColor = AppColor.Primary,
//                            modifier = Modifier
//                                .height(70.dp)
//                                .fillMaxWidth()
//                                .align(Alignment.BottomCenter),
//                            //elevation = 10.dp, border = BorderStroke(1.dp, Color.White),
//                            shape = RoundedCornerShape(
//                                topStart = 10.dp,
//                                topEnd = 10.dp,
//                                bottomEnd = 0.dp,
//                                bottomStart = 0.dp
//                            ),
//                        )
//                        {
//
//                        }
//
//                        OutlinedTextField(
//                            value = "Enter Message...",
//                            onValueChange = {},
//                            textStyle = LocalTextStyle.current.copy(
//                                textAlign = TextAlign.Start,
//                                color = Color.Gray
//                            ),
//                            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
//                            keyboardActions = KeyboardActions(
//                                onDone = { focusManager.clearFocus() }
//                            ),
//                            colors = TextFieldDefaults.outlinedTextFieldColors(
//                                backgroundColor = Color.White
//                            ),
//                            shape = RoundedCornerShape(10.dp),
//                            modifier = Modifier
//                                .size(width = 335.dp, height = 60.dp)
//                                .align(Alignment.BottomStart)
//                                .padding(start = 15.dp, bottom = 10.dp)
//                        )
//
//
//
//                        Button(
//                            onClick = { /*TODO*/ },
//                            colors = ButtonDefaults.buttonColors(
//                                backgroundColor = Color.White
//                            ),
//                            contentPadding = PaddingValues(10.dp),
//                            shape = CircleShape,
//                            modifier = Modifier
//                                .align(Alignment.BottomEnd)
//                                .padding(end = 12.dp, bottom = 10.dp)
//                                .size(50.dp)
//
//                        ) {
//                            Image(
//                                painterResource(id = R.drawable.iconsend),
//                                contentDescription = "Send Icon",
//                                modifier = Modifier.fillMaxSize()
//                            )
//                        }
//
//                    }
                }
            }
        }
    }

}