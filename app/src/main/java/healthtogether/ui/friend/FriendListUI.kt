package healthtogether.ui.friend

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
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
import androidx.compose.runtime.mutableStateListOf
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
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import android.util.Base64
import healthtogether.components.ShowLoading
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.log

@OptIn(ExperimentalMaterialApi::class, ExperimentalEncodingApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FriendListView(
    navController: NavController,
    viewModel: HomeViewModel,

) = AppTheme {
    val focusManager = LocalFocusManager.current
    var menuSelect by remember {
        mutableStateOf("friend_list")
    }

    var friendList by remember {
        mutableStateOf(listOf<User>())
    }

    var friendRequest by remember {
        mutableStateOf(listOf<User>())
    }

    var nameSearch by remember {
        mutableStateOf("")
    }

    var searchConfirm by remember {
        mutableStateOf("")
    }


    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(menuSelect) {
        isLoading = true
        if(menuSelect == "friend_list")
        {
            friendList = getDataFriendList(menuSelect)
            isLoading = false
        }

        else if (menuSelect == "friend_request")
        {
            friendRequest = getDataFriendList(menuSelect)
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
                        text = "Friend",
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
                            Text(text = "Friend",
                                textAlign = TextAlign.Center,
                                color = if (menuSelect == "friend_list") AppColor.Primary else AppColor.BorderGrey,
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .fillMaxWidth(.5f)
                                    .clickable {
                                        menuSelect = "friend_list"

                                    }


                            )

                            Text(text = "Friend Request",
                                textAlign = TextAlign.Center,
                                color = if (menuSelect == "friend_request") AppColor.Primary else AppColor.BorderGrey,
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically)
                                    .clickable {
                                        menuSelect = "friend_request"

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
                            value = nameSearch,
                            onValueChange = { nameSearch = it },
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
                                value = nameSearch,
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
                                searchConfirm = nameSearch
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

                        if (menuSelect == "friend_list") {
                            Log.v("TestFriendList", "Friend Select")
                                                friendList.forEach{ item ->

                                                    if ((item.name == searchConfirm) || searchConfirm == "")
                                                Button(
                                                    onClick = {
                                                            navController.navigate(
                                                                OutdoorExerciseScreen.MemberProfile.routeWithMemberId(
                                                                    item.id
                                                                )
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
                                                    val imageBytes = Base64.decode(item.image, Base64.DEFAULT)
                                                    val decodedImage = BitmapFactory.decodeByteArray(
                                                        imageBytes,
                                                        0,
                                                        imageBytes.size
                                                    )

                                                    Image(
                                                        painter = if (item.image.isNotEmpty()) rememberAsyncImagePainter(
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
                                                            text = item.name,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.W400,
                                                            color = AppColor.Primary,
                                                        )

                                                        Spacer(modifier = Modifier.size(10.dp))

                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                        ) {
                                                            Button(
                                                                onClick = {
                                                                    //navController.navigate(OutdoorExerciseScreen.GroupRoom.routeWithGroupRoomId(item.cid))
                                                                },
                                                                colors = ButtonDefaults.buttonColors(AppColor.Danger),
                                                                modifier = Modifier
                                                                    .height(30.dp)
                                                                    .padding(
                                                                        start = 0.dp,
                                                                        end = 15.dp,
                                                                        bottom = 0.dp
                                                                    ),
                                                                contentPadding = PaddingValues(0.dp)
                                                            ) {
                                                                Text(text = " Unfriend ")
                                                            }



                                                        }

                                                    }


                                                }
                                            }


                        } else if (menuSelect == "friend_request") {
                            Log.v("TestFriendList", "FriendRequest")

                            friendRequest.forEach{ item ->
                                if ((item.name == searchConfirm) || searchConfirm == "")
                                Button(
                                    onClick = {
                                        navController.navigate(
                                            OutdoorExerciseScreen.MemberProfile.routeWithMemberId(
                                                item.id
                                            )
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
                                    val imageBytes = Base64.decode(item.image, Base64.DEFAULT)
                                    val decodedImage = BitmapFactory.decodeByteArray(
                                        imageBytes,
                                        0,
                                        imageBytes.size
                                    )

                                    Image(
                                        painter = if (item.image.isNotEmpty()) rememberAsyncImagePainter(
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
                                            text = item.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.W400,
                                            color = AppColor.Primary,
                                        )

                                        Spacer(modifier = Modifier.size(10.dp))



                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Button(
                                                onClick = {
                                                    //navController.navigate(OutdoorExerciseScreen.GroupRoom.routeWithGroupRoomId(item.cid))
                                                },
                                                colors = ButtonDefaults.buttonColors(AppColor.Primary),
                                                modifier = Modifier
                                                    .height(30.dp)
                                                    .padding(
                                                        start = 0.dp,
                                                        end = 15.dp,
                                                        bottom = 0.dp
                                                    ),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Text(text = " Accept ")
                                            }

                                            Button(
                                                onClick = {
                                                    //navController.navigate(OutdoorExerciseScreen.GroupRoom.routeWithGroupRoomId(item.cid))
                                                },
                                                colors = ButtonDefaults.buttonColors(AppColor.Danger),
                                                modifier = Modifier
                                                    .height(30.dp)
                                                    .padding(
                                                        start = 0.dp,
                                                        end = 15.dp,
                                                        bottom = 0.dp
                                                    ),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Text(text = " Cancel ")
                                            }



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
suspend fun getDataFriendList(menu: String): List<User> {
    val request = QueryUsersRequest(
        filter =
        Filters.and(
            Filters.`in`("id", ChatClient.instance().getCurrentUser()!!.extraData.get(menu).toString().removePrefix("[")
                .removeSuffix("]").split(",").map { it.trim()} ),
        )
        ,
        offset = 0,
        limit = 10,
    )


    return suspendCancellableCoroutine { continuation ->
        ChatClient.instance().queryUsers(request).enqueue() { result ->
            if (result.isSuccess) {
                // Resume the coroutine with the result
                continuation.resume(result.data())
                Log.v("FriendListPage","success : ${result.data()}")

            } else {
                // If there's an error, resume with an exception
                continuation.resumeWithException(Exception("Query failed: ${result.error().message}"))
                Log.v("FriendListPage","fail : ${result.error().message}")
            }
        }

        // If the coroutine is cancelled, ensure that the enqueue callback does not continue executing
        continuation.invokeOnCancellation {
            // Clean up if needed
        }
    }

}