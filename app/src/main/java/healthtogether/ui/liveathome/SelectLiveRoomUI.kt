package healthtogether.ui.liveathome

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.apiService.model.Dropdowns.BaseDropdownViewModel
import healthtogether.components.AppColor
import healthtogether.components.BottomMenu
import healthtogether.components.ExerciseAtHomeScreen
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.ShowLoading
import healthtogether.components.TopAppNameAndProfile
import healthtogether.components.getExerciseList
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.liveathome.livemodel.LiveViewModel
import healthtogether.ui.outdoorexercise.outdoormodel.OutdoorRoomSearchData
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.Member
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectLiveRoomView(
    navController: NavController,
    viewModel: HomeViewModel,
    channel: ChannelListViewModel,
) =
    AppTheme {

        var roomName by remember {
            mutableStateOf("")
        }

        var exerciseSelect by remember {
            mutableStateOf("All")
        }

        var showExerciseCategory by remember {
            mutableStateOf(false)
        }



        var exerciseCategoryItem by remember {
            mutableStateOf(
                listOf(BaseDropdownViewModel())
            )
        }

        LaunchedEffect (Unit){
            exerciseCategoryItem = getExerciseList("EN") + BaseDropdownViewModel("99","All")
        }





        var listChannel by remember {
            mutableStateOf(listOf<Channel>())
        }


        val roomNameInteractionSource = remember { MutableInteractionSource() }

        val exerciseInteractionSource = remember { MutableInteractionSource() }


        var isLoading by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            isLoading = true
            listChannel = channel.getChannel(
                "live"

            )
            isLoading = false
        }
        //createRoomID(searchData.province,searchData.district,searchData.subDistrict,searchData.place)


        val focusManager = LocalFocusManager.current
        val context = LocalContext.current

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
                    .height(700.dp),
                elevation = 20.dp,
                border = BorderStroke(0.dp, Color.White),
                shape = RoundedCornerShape(30.dp),

                ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    //horizontalAlignment = Alignment.CenterHorizontally

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
                            text = "Live Group",
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W800,
                            color = AppColor.Primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        )


                    }

                    Spacer(Modifier.size(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Search Room",
                            color = AppColor.Primary,
                            fontWeight = FontWeight.W600,

                        )

                        BasicTextField(
                            value = roomName,
                            onValueChange = { roomName = it },
                            readOnly = false,
                            interactionSource = roomNameInteractionSource,
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
                                .size(width = 200.dp, height = 40.dp),
                        ) {
                            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                value = roomName,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = roomNameInteractionSource,
                                contentPadding = PaddingValues(
                                    start = 15.dp,
                                    end = 30.dp
                                ),
                                placeholder = {
                                    Text(
                                        text = "Room Name",
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
                                        interactionSource = roomNameInteractionSource,
                                        shape = RoundedCornerShape(15.dp),
                                        unfocusedBorderThickness = 1.dp,
                                        focusedBorderThickness = 2.dp
                                    )
                                }
                            )
                        }

                        //if(channel.getUser().getCurrentUser()?.extraData?.get("Status") == "Trainer")
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AppColor.Primary
                            ),
                            modifier = Modifier
                                ,
                            shape = RoundedCornerShape(20.dp),
                            onClick = {
                                navController.navigate(
                                    ExerciseAtHomeScreen.CreateLiveRoom.route
                                )
                            },
                        ) {
                            Image(
                                painterResource(id = R.drawable.iconaddgroup),
                                contentDescription = "CreateGroup Icon",
                                contentScale = ContentScale.Crop
                            )
                        }


                    }

                    Spacer(Modifier.size(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = showExerciseCategory,
                        onExpandedChange = { showExerciseCategory = !showExerciseCategory },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .align(Alignment.CenterHorizontally),
                    ) {
                        BasicTextField(
                            value = exerciseSelect,
                            onValueChange = {},
                            readOnly = true,
                            interactionSource = exerciseInteractionSource,
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth(.9f)
                                .height(40.dp),
                        ) {
                            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                value = exerciseSelect,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = exerciseInteractionSource,
                                contentPadding = PaddingValues(
                                    start = 50.dp,
                                    end = 0.dp
                                ),
                                placeholder = {
                                    Text(
                                        text = "Select Exercise",
                                        textAlign = TextAlign.Center,
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
                                        interactionSource = exerciseInteractionSource,
                                        shape = RoundedCornerShape(15.dp),
                                        unfocusedBorderThickness = 1.dp,
                                        focusedBorderThickness = 2.dp
                                    )
                                }
                            )
                        }

                        ExposedDropdownMenu(
                            expanded = showExerciseCategory,
                            onDismissRequest = { showExerciseCategory = false }
                        ) {
                            exerciseCategoryItem.forEach { item ->

                                DropdownMenuItem(onClick = {
                                    exerciseSelect = item.text
                                    showExerciseCategory = false
                                }
                                ) {
                                    Text(text = item.text, fontSize = 15.sp)
                                }
                            }
                        }
                    }

                    Divider(color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.size(10.dp))

                    if(isLoading)
                        ShowLoading()

                    else
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                            listChannel.forEach { item ->

                                Log.v("LiveList", item.name)

                                if ((roomName == "" || item.name.startsWith(roomName)) && (exerciseSelect == "All" || exerciseSelect == item.extraData.get(
                                        "exercise"
                                    ))
                                )
                                    Button(
                                        onClick = {

                                                channel.getUser().addMembers(
                                                    "messaging",
                                                    item.id,
                                                    listOf(viewModel.state.userID)
                                                ).enqueue { result ->
                                                    if (result.isSuccess) {
                                                        Log.v("TestAddMember", "Success")
                                                        navController.navigate(
                                                            ExerciseAtHomeScreen.LiveRoom.routeWithGroupRoomId(
                                                                item.cid
                                                            )
                                                        )
                                                    } else {
                                                        Log.v("TestAddMember", "Error")
                                                        Log.v(
                                                            "TestAddMember",
                                                            "${result.error().message}"
                                                        )
                                                    }
                                                }


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
                                                    text = item.name,
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


                                                Row(
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .padding(end = 30.dp)
                                                ) {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.iconviewer),
                                                        contentDescription = "Viewer Icon",
                                                        modifier = Modifier
                                                            .padding(end = 5.dp)
                                                            .align(Alignment.CenterVertically)
                                                            .size(20.dp),
                                                    )

                                                    Text(
                                                        text = "${item.memberCount}",
                                                        color = AppColor.Primary,
                                                        fontWeight = FontWeight.W700,
                                                        fontSize = 18.sp,
                                                        modifier = Modifier
                                                            .padding(end = 0.dp)
                                                    )
                                                }


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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateLiveRoomView(
    navController: NavController,
    viewModel: HomeViewModel,
    liveViewModel: LiveViewModel,
    channel: ChannelListViewModel,
) = AppTheme {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var roomName by rememberSaveable {
        mutableStateOf("")
    }

    var numberMember: String by rememberSaveable {
        mutableStateOf("")
    }

    var showExerciseCategory by remember {
        mutableStateOf(false)
    }

    var exerciseSelect by remember {
        mutableStateOf("")
    }


    var exerciseCategoryItem by remember {
        mutableStateOf(
            listOf(BaseDropdownViewModel())
        )
    }

    LaunchedEffect(Unit) {
        exerciseCategoryItem = getExerciseList("EN") + BaseDropdownViewModel("99","All")
    }

    val roomNameInteractionSource = remember { MutableInteractionSource() }
    val exerciseInteractionSource = remember { MutableInteractionSource() }

    var timeStart: String by rememberSaveable { mutableStateOf("None") }
    var timeEnd: String by rememberSaveable { mutableStateOf("None") }
    var calendar = Calendar.getInstance()
    var startHour: Int by remember {
        mutableStateOf(calendar[Calendar.HOUR_OF_DAY])
    }
    var startMinute: Int by remember {
        mutableStateOf(calendar[Calendar.MINUTE])
    }
    var endHour: Int by remember {
        mutableStateOf(23)
    }
    var endMinute: Int by remember {
        mutableStateOf(0)
    }

    val setStartTime = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            timeStart = if (hour < 10)
                "0$hour:"
            else
                "$hour:"

            timeStart += if (minute < 10)
                "0$minute"
            else
                "$minute"

            if (hour < 6) {
                timeStart = "None"
                startHour = calendar[Calendar.HOUR_OF_DAY]
                startMinute = calendar[Calendar.MINUTE]
            } else if (hour > endHour || hour < calendar[Calendar.HOUR_OF_DAY]) {
                timeStart = "None"
                startHour = calendar[Calendar.HOUR_OF_DAY]
                startMinute = calendar[Calendar.MINUTE]

            } else if (hour == endHour && minute >= endMinute || hour == calendar[Calendar.HOUR_OF_DAY] && minute < calendar[Calendar.MINUTE]) {
                timeStart = "None"
                startHour = calendar[Calendar.HOUR_OF_DAY]
                startMinute = calendar[Calendar.MINUTE]

            } else {
                startHour = hour
                startMinute = minute
            }

        },
        startHour, startMinute, true,
    )

    val setEndTime = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            timeEnd = if (hour < 10)
                "0$hour:"
            else
                "$hour:"
            timeEnd += if (minute < 10)
                "0$minute"
            else
                "$minute"

            if (hour == 23 && minute > 0) {
                timeEnd = "None"
                endHour = 23
                endMinute = 0
            } else if (startHour > hour) {
                timeEnd = "None"
                endHour = 23
                endMinute = 0

            } else if (startHour == hour && startMinute >= minute) {
                timeEnd = "None"
                endHour = 23
                endMinute = 0

            } else {
                endHour = hour
                endMinute = minute
            }
        },
        endHour, endMinute, true,
    )

    var lat: Double? by remember {
        mutableStateOf(null)
    }

    lat = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<Double>("lat")

    var lng: Double? by remember {
        mutableStateOf(null)
    }

    lng = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<Double>("lng")

    var roomID_ByCreate:String? = null
    var createButtonClick by remember {
        mutableStateOf(false)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    if(createButtonClick)
        LaunchedEffect(Unit) {
            isLoading = true
            roomID_ByCreate = channel.createLiveChannel(
                roomName,
                exercise = exerciseSelect,
                startTime = timeStart,
                endTime = timeEnd,
                navController = navController,
                liveViewModel = liveViewModel
            )
            isLoading = false

            if (roomID_ByCreate != null)
            {
                navController.popBackStack()
                navController.navigate(ExerciseAtHomeScreen.LiveRoom.routeWithGroupRoomId(roomID_ByCreate.toString()))
            }
        }

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
                        text = "Create Live Group",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W800,
                        color = AppColor.Primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )


                }

                Spacer(modifier = Modifier.height(10.dp))

                if (isLoading)
                    ShowLoading()

                else
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(550.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = roomName,
                        onValueChange = { roomName = it },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        ),
                        placeholder = {
                            Text(
                                text = "Room Name",
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                modifier = Modifier.fillMaxSize()
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .size(width = 350.dp, height = 50.dp)
                            .align(Alignment.CenterHorizontally)

                    )


                    ExposedDropdownMenuBox(
                        expanded = showExerciseCategory,
                        onExpandedChange = { showExerciseCategory = !showExerciseCategory },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                    ) {
                        BasicTextField(
                            value = exerciseSelect,
                            onValueChange = {},
                            readOnly = true,
                            interactionSource = exerciseInteractionSource,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 15.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .size(width = 350.dp, height = 50.dp),
                        ) {
                            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                value = exerciseSelect,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = exerciseInteractionSource,
                                contentPadding = PaddingValues(
                                    start = 50.dp,
                                    end = 30.dp
                                )
                                ,
                                placeholder = {
                                    Text(
                                        text = "Select Exercise",
                                        textAlign = TextAlign.Start,
                                        color = Color.Gray,
                                        modifier = Modifier.fillMaxSize()
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
                                        interactionSource = exerciseInteractionSource,
                                        shape = RoundedCornerShape(10.dp),
                                        unfocusedBorderThickness = 1.dp,
                                        focusedBorderThickness = 2.dp
                                    )
                                }
                            )
                        }

                        ExposedDropdownMenu(
                            expanded = showExerciseCategory,
                            onDismissRequest = { showExerciseCategory = false }
                        ) {
                            exerciseCategoryItem.forEach { item ->

                                DropdownMenuItem(onClick = {
                                    exerciseSelect = item.text
                                    showExerciseCategory = false
                                }
                                ) {
                                    Text(text = item.text, fontSize = 15.sp)
                                }
                            }
                        }
                    }


                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Start Time ",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.W600,
                            color = AppColor.Primary,
                            modifier = Modifier
                                .padding(top = 35.dp, start = 25.dp)
                        )

                        OutlinedTextField(
                            value = timeStart,
                            onValueChange = { timeStart = it },
                            readOnly = true,
                            enabled = false,
                            placeholder = {
                                Text(
                                    text = "Set Start time",
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray,
                                    modifier = Modifier.fillMaxSize()
                                )
                            },
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .size(width = 200.dp, height = 50.dp)
                                .clickable {
                                    setStartTime.show()
                                }

                        )

                        Image(painterResource(id = R.drawable.iconcalender),
                            contentDescription = "Calender Icon",
                            modifier = Modifier
                                .clickable {
                                    setStartTime.show()
                                }
                                .padding(start = 5.dp, top = 15.dp)
                                .size(40.dp)
                                .align(Alignment.CenterVertically)
                        )

                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "End Time   ",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.W600,
                            color = AppColor.Primary,
                            modifier = Modifier
                                .padding(top = 35.dp, start = 25.dp)
                        )

                        OutlinedTextField(
                            value = timeEnd,
                            onValueChange = { timeEnd = it },
                            readOnly = true,
                            enabled = false,
                            placeholder = {
                                Text(
                                    text = "Set End Time",
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray,
                                    modifier = Modifier.fillMaxSize()
                                )
                            },
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            ),

                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .size(width = 200.dp, height = 50.dp)
                                .clickable { setEndTime.show() }

                        )

                        Image(painterResource(id = R.drawable.iconcalender),
                            contentDescription = "Calender Icon",
                            modifier = Modifier
                                .clickable {
                                    setEndTime.show()
                                }
                                .padding(start = 5.dp, top = 15.dp)
                                .size(40.dp)
                                .align(Alignment.CenterVertically)
                        )

                    }



                    Button(
                        onClick = {
                            createButtonClick = true

                        },
                        enabled = !(roomName.isNullOrEmpty() || exerciseSelect.isNullOrEmpty()  || timeStart == "None" || timeEnd == "None"),
                        colors = ButtonDefaults.buttonColors(AppColor.Primary),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(top = 25.dp)
                            .height(50.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Confirm",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W700,
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))


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