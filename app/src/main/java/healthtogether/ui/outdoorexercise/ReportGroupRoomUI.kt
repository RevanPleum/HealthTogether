package healthtogether.ui.outdoorexercise


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.components.AppColor
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
fun ReportGroupView(
    navController: NavController,
    viewModel: HomeViewModel,
    messageViewModel: MessageListViewModel
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

    var showComplaintItem by remember {
        mutableStateOf(false)
    }

    var showMemberItem by remember {
        mutableStateOf(false)
    }

    var memberSelect by remember {
        mutableStateOf("Select Member")
    }

    var complaintSelect by remember {
        mutableStateOf("")
    }

    var complaintList = listOf("Complaint1", "Complaint2")

    val complaintInteractionSource = remember { MutableInteractionSource() }

    var complaintInfomation by remember {
        mutableStateOf("")
    }

    var imageSelect by rememberSaveable { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                imageSelect = it.toString()
            }
        }
    )

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
                .fillMaxSize(),
            elevation = 20.dp,
            border = BorderStroke(0.dp, Color.White),
            shape = RoundedCornerShape(30.dp),

            ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                        text = "Report",
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


                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = "Group Name : ${messageViewModel.channel.name}",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.W600,
                    color = AppColor.Primary,
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .align(Alignment.Start)
                )

                Spacer(modifier = Modifier.size(10.dp))

                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Member or Trainer Name : ",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.W600,
                        color = AppColor.Primary,
                        modifier = Modifier
                            .padding(start = 30.dp)


                    )

                    ExposedDropdownMenuBox(
                        expanded = showMemberItem,
                        onExpandedChange = { showMemberItem = !showMemberItem },
                        modifier = Modifier.padding(bottom = 10.dp)

                    ) {
                        Text(
                            text = memberSelect,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.W600,
                            color = AppColor.Primary,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp)
                        )


                        ExposedDropdownMenu(
                            expanded = showMemberItem,
                            onDismissRequest = { showMemberItem = false }
                        ) {
                            messageViewModel.channel.members.forEach { item ->

                                DropdownMenuItem(onClick = {
                                    memberSelect = item.user.name
                                    showMemberItem = false


                                }
                                ) {
                                    Text(text = item.user.name, fontSize = 15.sp)
                                }
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.size(10.dp))

                ExposedDropdownMenuBox(
                    expanded = showComplaintItem,
                    onExpandedChange = { showComplaintItem = !showComplaintItem },
                    modifier = Modifier.padding(bottom = 10.dp)

                ) {
                    BasicTextField(
                        value = complaintSelect,
                        onValueChange = {},
                        readOnly = true,
                        interactionSource = complaintInteractionSource,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 15.sp,
                            color = Color.Black
                        ),

                        modifier = Modifier.size(width = 330.dp, height = 50.dp)

                    ) {
                        TextFieldDefaults.OutlinedTextFieldDecorationBox(
                            value = complaintSelect,
                            innerTextField = it,
                            enabled = true,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = complaintInteractionSource,
                            contentPadding = PaddingValues(start = 15.dp, end = 30.dp),
                            placeholder = {
                                Text(
                                    text = "Select Complaint",
                                    textAlign = TextAlign.Start,
                                    color = Color.Gray,
                                )
                            },
                            trailingIcon =
                            {
                                IconButton(
                                    onClick = { }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.dropdown_icon),
                                        contentDescription = "Dropdown Icon",
                                        tint = AppColor.Primary
                                    )
                                }
                            },
                            border = {
                                TextFieldDefaults.BorderBox(
                                    enabled = true,
                                    isError = false,
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        unfocusedBorderColor = Color.Gray,
                                        focusedBorderColor = AppColor.Primary
                                    ),
                                    interactionSource = complaintInteractionSource,
                                    shape = RoundedCornerShape(15.dp),
                                    unfocusedBorderThickness = 1.dp,
                                    focusedBorderThickness = 2.dp
                                )
                            }
                        )
                    }


                    ExposedDropdownMenu(
                        expanded = showComplaintItem,
                        onDismissRequest = { showComplaintItem = false }
                    ) {
                        complaintList.forEach { item ->

                            DropdownMenuItem(onClick = {
                                complaintSelect = item
                                showComplaintItem = false


                            }
                            ) {
                                Text(text = item, fontSize = 15.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.size(15.dp))

                OutlinedTextField(
                    value = complaintInfomation,
                    onValueChange = { complaintInfomation = it },
                    placeholder = {
                        Text(
                            text = "More Information...",
                            textAlign = TextAlign.Start,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Start
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),

                    modifier = Modifier
                        .size(width = 350.dp, height = 300.dp)
                )

                Spacer(modifier = Modifier.size(30.dp))

                Text(
                    text = "Attach Image",
                    color = AppColor.Primary,
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        imagePicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    text = imageSelect,
                    color = AppColor.Primary,
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clickable {
                            imageSelect = ""
                        }
                        .fillMaxWidth(.8f)
                )

                Spacer(modifier = Modifier.size(20.dp))


                Button(
                    onClick = {


                    },
                    colors = ButtonDefaults.buttonColors(AppColor.Primary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 15.dp)
                ) {
                    Text(
                        text = "Send Feedback",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W700,
                    )
                }


            }
        }
    }
}