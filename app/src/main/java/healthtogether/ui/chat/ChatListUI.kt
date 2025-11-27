package healthtogether.ui.chat

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.heathtogethermobile.R
import healthtogether.components.AppColor
import healthtogether.components.BottomMenu
import healthtogether.components.HomeScreen
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.ShowLoading
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField.Companion.descByName
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.Member
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatListView(
    navController: NavController,
    viewModel: HomeViewModel,
) = AppTheme {
    val focusManager = LocalFocusManager.current
    var menuSelect by remember {
        mutableStateOf("FriendChatList")
    }

    var chatFriendChannel by remember {
        mutableStateOf(listOf<Channel>())
    }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(menuSelect) {
        isLoading = true
            if(menuSelect == "FriendChatList")
            {
                chatFriendChannel = getChatChannel(menuSelect)
                isLoading = false
            }

            else if (menuSelect == "MyGroupChatList")
            {
                chatFriendChannel = getChatChannel(menuSelect)
                isLoading = false
            }
    }


    Surface {
        Modifier.fillMaxSize()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            TopAppNameAndProfile(navController, viewModel.state.imageProfile)
        }

        Card(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize(),
            elevation = 20.dp,
            border = BorderStroke(0.dp, Color.White),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomMenu(state = viewModel.state, navController = navController)
                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.size(20.dp))

                    Text(
                        text = "Chat",
                        color = AppColor.Primary,
                        fontWeight = FontWeight.W700,
                        fontSize = 20.sp,
                    )


                    Spacer(modifier = Modifier.size(20.dp))

                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .width(1.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    )
                    {
                        Divider(
                            color = Color.LightGray,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .align(Alignment.TopCenter)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Text(text = "Chat",
                                textAlign = TextAlign.Center,
                                color = if (menuSelect == "FriendChatList") AppColor.Primary else AppColor.BorderGrey,
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .fillMaxWidth(.5f)
                                    .clickable {
                                        menuSelect = "FriendChatList"

                                    }


                            )

                            Text(text = "Chat Group",
                                textAlign = TextAlign.Center,
                                color = if (menuSelect == "MyGroupChatList") AppColor.Primary else AppColor.BorderGrey,
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically)
                                    .clickable {
                                        menuSelect = "MyGroupChatList"

                                    }
                            )
                        }
                    }

                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .width(1.dp)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(.9f),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {


                        Text(
                            text = "Search",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W500,
                            color = AppColor.Primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 0.dp)
                                .align(Alignment.CenterVertically)
                        )

                        val memberNameInteractionSource = remember { MutableInteractionSource() }



                        BasicTextField(
                            value = "",
                            onValueChange = { it },
                            readOnly = false,
                            interactionSource = memberNameInteractionSource,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 15.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            modifier = Modifier
                                .size(width = 200.dp, height = 40.dp)
                                .align(Alignment.CenterVertically)
                            //.padding(bottom = 10.dp),
                        ) {
                            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                value = "",
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = memberNameInteractionSource,
                                contentPadding = PaddingValues(
                                    start = 15.dp,
                                    end = 30.dp
                                ),
                                placeholder = {
                                    Text(
                                        text = "Name",
                                        textAlign = TextAlign.Center,
                                        color = Color.Gray,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                border = {
                                    TextFieldDefaults.BorderBox(
                                        enabled = true,
                                        isError = false,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            unfocusedBorderColor = Color.Gray,
                                            focusedBorderColor = AppColor.Primary
                                        ),
                                        interactionSource = memberNameInteractionSource,
                                        shape = RoundedCornerShape(15.dp),
                                        unfocusedBorderThickness = 1.dp,
                                        focusedBorderThickness = 2.dp
                                    )
                                }
                            )
                        }



                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AppColor.Primary
                            ),
                            shape = RoundedCornerShape(20.dp),
                            onClick = {

                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Image(
                                painterResource(id = R.drawable.iconsearch),
                                contentDescription = "Search Icon",
                                contentScale = ContentScale.Crop
                            )
                        }


                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .width(1.dp)
                    )

                    if (isLoading)
                        ShowLoading()

                    else
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .fillMaxHeight(.9f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.size(10.dp))


                        if (menuSelect == "FriendChatList") {
                            Log.v("TestChatList", "Chat Select")

                            if (chatFriendChannel != null)
                            chatFriendChannel.forEach{ item ->
                                var userFriend by remember {
                                    mutableStateOf<User?>(null)
                                }

                                LaunchedEffect(Unit) {
                                  val memberList = getMemberList(item.id)

                                    userFriend = if (memberList[0].user.id != ChatClient.instance().getCurrentUser()!!.id)
                                        memberList[0].user
                                    else
                                        memberList[1].user
                                }

                                if(userFriend != null)
                                    Button(
                                        onClick = {
                                            navController.navigate(
                                                HomeScreen.ChatFriendScreen.routeWithID(item.cid, userFriend!!.id)
                                            )


                                        },
                                        colors = ButtonDefaults.buttonColors(Color.White),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .padding(
                                                top = 10.dp,
                                                start = 15.dp,
                                                bottom = 0.dp
                                            ),
                                        contentPadding = PaddingValues(0.dp)

                                    ) {
                                        Spacer(modifier = Modifier.size(10.dp))
                                        val imageBytes = Base64.decode(userFriend!!.image, Base64.DEFAULT)
                                        val decodedImage = BitmapFactory.decodeByteArray(
                                            imageBytes,
                                            0,
                                            imageBytes.size
                                        )

                                        Image(
                                            painter = if (userFriend!!.image.isNotEmpty()) rememberAsyncImagePainter(
                                                decodedImage
                                            ) else
                                                painterResource(id = R.drawable.profile),
                                            contentDescription = "Profile Picture",
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    BorderStroke(1.dp, Color.White),
                                                    CircleShape
                                                ),
                                            contentScale = ContentScale.Crop,
                                        )

                                        Spacer(modifier = Modifier.size(20.dp))

                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(
                                                    start = 10.dp,
                                                    top = 10.dp,
                                                    bottom = 10.dp
                                                ),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = userFriend!!.name,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.W400,
                                                color = AppColor.Primary,
                                            )

                                            Spacer(modifier = Modifier.size(10.dp))

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {




                                            }

                                        }


                                    }
                            }


                        } else if (menuSelect == "MyGroupChatList") {
                            Log.v("TestChatList", "ChatGroup Select")
                            //

                            chatFriendChannel.forEach{ item ->
                                Button(
                                    onClick = {
                                                navController.navigate(
                                                    OutdoorExerciseScreen.GroupRoom.routeWithGroupRoomId(
                                                        item.cid
                                                    )
                                                )

                                    },
                                    colors = ButtonDefaults.buttonColors(Color.White),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                                    contentPadding = PaddingValues(0.dp)

                                ) {
                                    Card(
                                        modifier = Modifier
                                            .height(100.dp)
                                            .width(10.dp),
                                        shape = RoundedCornerShape(
                                            topStart = 30.dp,
                                            topEnd = 30.dp,
                                            bottomEnd = 30.dp,
                                            bottomStart = 30.dp
                                        ), backgroundColor = AppColor.Primary
                                    ) {}
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 10.dp, top = 7.dp, bottom = 7.dp),
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
                                                text = "${item.name}",
                                                color = AppColor.Primary,
                                                textAlign = TextAlign.Center,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.W400,
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
                                                text = "Category : ",
                                                color = AppColor.Primary,
                                                fontWeight = FontWeight.W700,
                                                fontSize = 18.sp,
                                            )

                                            Text(
                                                text = "${item.extraData.get("exercise")}",
                                                color = AppColor.Primary,
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.W400,
                                                fontSize = 18.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )

                                            Text(
                                                text = "${item.memberCount}/${
                                                    item.extraData.get("maxMember").toString()
                                                        .toDoubleOrNull()?.roundToInt()
                                                }",
                                                color = AppColor.Primary,
                                                textAlign = TextAlign.End,
                                                fontWeight = FontWeight.W700,
                                                fontSize = 18.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(end = 20.dp)
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
                                                fontWeight = FontWeight.W500,
                                                fontSize = 18.sp,
                                            )

                                            val roomTime =
                                                item.extraData.get("time") as? Map<String, Any>
                                            Text(
                                                text = "${roomTime?.get("timeStart")} - ${
                                                    roomTime?.get(
                                                        "timeEnd"
                                                    )
                                                }",
                                                color = AppColor.Primary,
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.W400,
                                                fontSize = 18.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }


                                }
                            }

                        }

                        Spacer(modifier = Modifier.size(10.dp))
                    }


                }

            }


        }


    }
}


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun getChatChannel(menu: String): List<Channel> {
    val request = QueryChannelsRequest(

        filter = if (menu == "FriendChatList") {
            Filters.and(
                Filters.eq("type", "messaging"),
                Filters.`in`("members", ChatClient.instance().getCurrentUser()!!.id),
                Filters.notExists("exercise")

            )
        }
        else{
            Filters.and(
                Filters.eq("type", "messaging"),
                Filters.`in`("members", ChatClient.instance().getCurrentUser()!!.id),
                Filters.exists("placeAddress")
            )
        },
        offset = 0,
        limit = 10,
        //querySort = QuerySortByField.descByName("create_at")
    ).apply {
        watch = true
        state = true
    }

    return suspendCancellableCoroutine { continuation ->
        ChatClient.instance().queryChannels(request).enqueue { result ->
            if (result.isSuccess) {
                // Resume the coroutine with the result
                Log.v("ChatListPage","Success")
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

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun getMemberList(cID : String): List<Member> {
    val offset = 0
    val limit = 10
    val filterByName = Filters.neutral()
    val sort = QuerySortByField<Member>().descByName("userId")

    return suspendCancellableCoroutine { continuation ->
        ChatClient.instance().queryMembers("messaging",cID, offset = offset, limit = limit, filter = filterByName , sort = sort).enqueue { result ->
            if (result.isSuccess) {
                // Resume the coroutine with the result
                Log.v("ChatListPage","Success : ${result.data()}")
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
