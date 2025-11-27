package healthtogether.ui.outdoorexercise

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
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
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import io.getstream.chat.android.client.events.MemberRemovedEvent
import io.getstream.chat.android.client.subscribeFor
import io.getstream.chat.android.client.utils.observable.Disposable
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MemberListView(
    navController: NavController,
    viewModel: HomeViewModel,
    messageViewModel: MessageListViewModel,

) = AppTheme {


    var exitRoom by remember { mutableStateOf(false) }

    var disposable: Disposable = messageViewModel.chatClient.subscribeFor<MemberRemovedEvent>{
        if(it.user.id == messageViewModel.chatClient.getCurrentUser()!!.id) {
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

    val focusManager = LocalFocusManager.current

    var friendList =
        messageViewModel.chatClient.getCurrentUser()!!
            .extraData.get("exercise").toString().removePrefix("[")
            .removeSuffix("]").split(",").map { it.trim() }


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


        Card(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(650.dp),
            elevation = 20.dp,
            border = BorderStroke(0.dp, Color.White),
            shape = RoundedCornerShape(30.dp),

            ) {

            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painterResource(id = R.drawable.iconback),
                        contentDescription = "Back Icon",
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack()
                            }
                            .align(alignment = Alignment.CenterStart)
                            .padding(top = 20.dp, start = 10.dp))

                    Text(
                        text = "Member",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W800,
                        color = AppColor.Primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )

                }

                Spacer(modifier = Modifier.size(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(modifier = Modifier.size(5.dp))

                    Text(
                        text = "Search Member",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W400,
                        color = AppColor.Primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 0.dp)
                    )

                    val memberNameInteractionSource = remember { MutableInteractionSource() }

                    Spacer(modifier = Modifier.size(10.dp))

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

                    Spacer(modifier = Modifier.size(10.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColor.Primary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        onClick = {

                        },
                    ) {
                        Image(
                            painterResource(id = R.drawable.iconsearch),
                            contentDescription = "Search Icon",
                            contentScale = ContentScale.Crop
                        )
                    }


                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.size(10.dp))

                    Log.v("TestMemberList", "${messageViewModel.channel.members}")

                    messageViewModel.channel.members.forEach { item ->


                        Button(
                            onClick = {
                                if (item.user.id != viewModel.state.userID)
                                    navController.navigate(
                                        OutdoorExerciseScreen.MemberProfile.routeWithMemberId(
                                            item.user.id
                                        )
                                    )
                                else
                                    navController.navigate(HomeScreen.ProfileScreen.route)
                            },
                            colors = ButtonDefaults.buttonColors(Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .padding(top = 10.dp, start = 15.dp, bottom = 0.dp),
                            contentPadding = PaddingValues(0.dp)

                        ) {
                            Spacer(modifier = Modifier.size(10.dp))
                            val imageBytes = Base64.decode(item.user.image, Base64.DEFAULT)
                            val decodedImage = BitmapFactory.decodeByteArray(
                                imageBytes,
                                0,
                                imageBytes.size
                            )

                            Image(
                                painter = if (item.user.image.isNotEmpty()) rememberAsyncImagePainter(
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
                                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.user.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.W400,
                                    color = AppColor.Primary,
                                )

                                Spacer(modifier = Modifier.size(10.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    if(viewModel.state.userID != messageViewModel.chatClient.getCurrentUser()!!.id && !friendList.contains(item.user.id))
                                    Button(
                                        onClick = {
                                            //navController.navigate(OutdoorExerciseScreen.GroupRoom.routeWithGroupRoomId(item.cid))
                                        },
                                        colors = ButtonDefaults.buttonColors(AppColor.Primary),
                                        modifier = Modifier
                                            .height(30.dp)
                                            .padding(start = 0.dp, end = 15.dp, bottom = 0.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = "Add",
                                            color = Color.White,
                                        )
                                    }

                                    if (viewModel.state.userID == messageViewModel.channel.createdBy.id && viewModel.state.userID != item.user.id)
                                        Button(
                                            onClick = {
                                                messageViewModel.chatClient.removeMembers(
                                                    messageViewModel.channel.type,
                                                    messageViewModel.channel.id,
                                                    listOf(item.user.id)
                                                ).enqueue { result ->
                                                    if (result.isSuccess) {
                                                        Log.v("TestRemoveMember", "Success")

                                                    } else
                                                        Log.v(
                                                            "TestRemoveMember",
                                                            "fail : ${result.error().message}"
                                                        )
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(AppColor.Danger),
                                            modifier = Modifier
                                                .height(30.dp)
                                                .padding(start = 0.dp, end = 15.dp, bottom = 0.dp),
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Text(
                                                text = "Quit",
                                                color = Color.White,
                                            )
                                        }


                                }

                            }


                        }
                    }


                }


            }


        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            BottomMenu(viewModel.state, navController)
        }
    }
}