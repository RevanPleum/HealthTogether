package healthtogether.ui.outdoorexercise


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
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.ShowLoading
import healthtogether.components.TopAppNameAndProfile
import healthtogether.components.getExerciseList
import healthtogether.components.getGenderList
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.outdoorexercise.outdoormodel.OutdoorRoomSearchData
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel
import healthtogether.ui.outdoorexercise.outdoormodel.channels.createRoomID
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.Member
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalTime
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectGroupView(
    navController: NavController,
    viewModel: HomeViewModel,
    channel: ChannelListViewModel,
    searchData: OutdoorRoomSearchData
) =
    AppTheme {

        var placeName: String = searchData.placeName
        var roomName: String = searchData.roomName
        var exerciseSelect: String = searchData.exercise
        var genderSelect: String = searchData.gender

        var listChannel by remember {
            mutableStateOf(listOf<Channel>())
        }

        var isLoading by remember { mutableStateOf(false) }

        val TimeUpChannel = mutableListOf<String>()

        LaunchedEffect(Unit) {
            isLoading = true
            listChannel = channel.getChannel(
                    searchData.province+
                    searchData.district+
                    searchData.subDistrict+
                    searchData.placeValue
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
                    .height(650.dp),
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
                            text = placeName,
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W800,
                            color = AppColor.Primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        )

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AppColor.Primary
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(top = 15.dp, end = 10.dp),
                            shape = RoundedCornerShape(20.dp),
                            onClick = {
                                val encodeToJson = Json.encodeToString(searchData)
                                navController.navigate(
                                    OutdoorExerciseScreen.CreateGroup.routeWithSearchData(
                                        encodeToJson
                                    )
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

                    if (isLoading)
                        ShowLoading()

                    else
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(550.dp)
                            .verticalScroll(rememberScrollState())
                    ) {



                        listChannel.forEach { item ->


                            var isMember = false
                            val offset = 0 // Use this value for pagination
                            val limit = 10
                            val sort = QuerySortByField<Member>()
                            val filterByName = Filters.eq("name", viewModel.state.userName)

                            val time = item.extraData.get("time") as? Map<*, *>

                            if (LocalTime.now() <= LocalTime.parse(time!!["timeEnd"].toString())) {
                                TimeUpChannel += item.cid
                                Log.v("TimeUp", LocalTime.now().toString())
                            }

                            channel.getUser()
                                .queryMembers(item.type, item.id, offset, limit, filterByName, sort)
                                .enqueue { result ->
                                    if (result.isSuccess) {
                                        if (result.data().toString() != "[]")
                                            isMember = true
                                        else
                                            Log.v("testQM", "member : none")
                                    } else {
                                        Log.v("testQM", "fail : ${result.error().message}")
                                    }
                                }




                            if((roomName == "none" || roomName == item.name) && (exerciseSelect == "none" || exerciseSelect == item.extraData.get("exercise")) && (genderSelect == "none" || genderSelect == item.extraData.get("gender")))
                            Button(
                                onClick = {

                                    if (item.memberCount < item.extraData.get("maxMember")
                                            .toString().toDouble().roundToInt() || isMember
                                    ) {
                                        if (!isMember) {
                                            channel.getUser().addMembers(
                                                "messaging",
                                                item.id,
                                                listOf(viewModel.state.userID)
                                            ).enqueue { result ->
                                                if (result.isSuccess) {
                                                    Log.v("TestAddMember", "Success")
                                                    navController.navigate(
                                                        OutdoorExerciseScreen.GroupRoom.routeWithGroupRoomId(
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
                                        } else
                                            navController.navigate(
                                                OutdoorExerciseScreen.GroupRoom.routeWithGroupRoomId(
                                                    item.cid
                                                )
                                            )
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
fun CreateGroupView(
    navController: NavController,
    viewModel: HomeViewModel,
    channel: ChannelListViewModel,
    searchData: OutdoorRoomSearchData
) = AppTheme {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var roomName by rememberSaveable {
        mutableStateOf("")
    }

    var numberMember: String by rememberSaveable {
        mutableStateOf("")
    }

    var showExerciseCategory by rememberSaveable {
        mutableStateOf(false)
    }

    var exerciseSelect by rememberSaveable {
        mutableStateOf("")
    }

    var exerciseCategoryItem by rememberSaveable {
        mutableStateOf(
            listOf(BaseDropdownViewModel())
        )
    }

    var showGenderCategory by rememberSaveable {
        mutableStateOf(false)
    }

    var genderSelect by rememberSaveable {
        mutableStateOf("")
    }

    var genderCategoryItem by rememberSaveable {
        mutableStateOf(
            listOf(BaseDropdownViewModel())
        )
    }



    var showPlaceCategory by rememberSaveable {
        mutableStateOf(false)
    }

    var placeSelect = rememberSaveable {
        searchData.placeName
    }

    LaunchedEffect(Unit) {
        exerciseCategoryItem = getExerciseList("EN")
        genderCategoryItem = getGenderList("EN")
    }

    val placeInteractionSource = remember { MutableInteractionSource() }
    val roomNameInteractionSource = remember { MutableInteractionSource() }
    val exerciseInteractionSource = remember { MutableInteractionSource() }
    val genderInteractionSource = remember { MutableInteractionSource() }

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


//    val timePickerState = rememberTimePickerState(is24Hour = true)
//    val showTimePicker = remember { mutableStateOf(false) }
//    val selectedTime = remember { mutableStateOf("") }

    var roomID_ByCreate:String? = null
    var createButtonClick  by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    if (createButtonClick)
    LaunchedEffect(Unit) {
        isLoading = true
        roomID_ByCreate = channel.createChannel(
            roomName,
            searchData.province,
            searchData.district,
            searchData.subDistrict,
            searchData.placeValue,
            exercise = exerciseSelect,
            startTime = timeStart,
            endTime = timeEnd,
            gender = genderSelect,
            maxMember = numberMember,
            lat = lat,
            lng = lng,
        )
        isLoading = false


        if (roomID_ByCreate != null)
            navController.popBackStack()
            navController.navigate(OutdoorExerciseScreen.GroupRoom.routeWithGroupRoomId(roomID_ByCreate.toString()))

        createButtonClick = false
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
                        text = "Create Group",
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
                                ),
                                placeholder = {
                                    Text(
                                        text = "Select Exercise",
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

                    ExposedDropdownMenuBox(
                        expanded = showGenderCategory,
                        onExpandedChange = { showGenderCategory = !showGenderCategory },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                    ) {
                        BasicTextField(
                            value = genderSelect,
                            onValueChange = {},
                            readOnly = true,
                            interactionSource = genderInteractionSource,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 15.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .size(width = 350.dp, height = 50.dp),
                        ) {
                            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                value = genderSelect,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = genderInteractionSource,
                                contentPadding = PaddingValues(
                                    start = 50.dp,
                                    end = 30.dp
                                ),
                                placeholder = {
                                    Text(
                                        text = "Select Gender",
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
                                        interactionSource = genderInteractionSource,
                                        shape = RoundedCornerShape(10.dp),
                                        unfocusedBorderThickness = 1.dp,
                                        focusedBorderThickness = 2.dp
                                    )
                                }
                            )
                        }

                        ExposedDropdownMenu(
                            expanded = showGenderCategory,
                            onDismissRequest = { showGenderCategory = false }
                        ) {
                            genderCategoryItem.forEach { item ->

                                DropdownMenuItem(onClick = {
                                    genderSelect = item.text
                                    showGenderCategory = false
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

                    OutlinedTextField(
                        value = numberMember,
                        onValueChange = { numberMember = it },
                        placeholder = {
                            Text(
                                text = "Number of Member",
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                modifier = Modifier.fillMaxSize()
                            )


                        },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .size(width = 350.dp, height = 50.dp)
                            .align(Alignment.CenterHorizontally)


                    )

                    Row {
                        ExposedDropdownMenuBox(
                            expanded = showPlaceCategory,
                            onExpandedChange = { showPlaceCategory = !showPlaceCategory },
                            // modifier = Modifier ,
                        ) {
                            BasicTextField(
                                value = placeSelect,
                                onValueChange = {},
                                readOnly = true,
                                interactionSource = placeInteractionSource,
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 15.sp,
                                    color = Color.Black
                                ),
                                modifier = Modifier
                                    .padding(top = 20.dp, start = 30.dp)
                                    .size(width = 250.dp, height = 50.dp),
                            ) {
                                TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                    value = placeSelect,
                                    innerTextField = it,
                                    enabled = true,
                                    singleLine = true,
                                    visualTransformation = VisualTransformation.None,
                                    interactionSource = genderInteractionSource,
                                    contentPadding = PaddingValues(
                                        start = 50.dp,
                                        end = 30.dp
                                    ),
                                    placeholder = {
                                        Text(
                                            text = "Select Place",
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
                                            interactionSource = placeInteractionSource,
                                            shape = RoundedCornerShape(10.dp),
                                            unfocusedBorderThickness = 1.dp,
                                            focusedBorderThickness = 2.dp
                                        )
                                    }
                                )
                            }


                        }

                        Button(
                            onClick = {
                                navController.navigate(
                                    OutdoorExerciseScreen.EditMarkerLocation.routeWithGroupRoomId(
                                        "None"
                                    )
                                )

                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(AppColor.Primary),
                            modifier = Modifier
                                .padding(start = 20.dp, top = 15.dp)
                                .size(width = 70.dp, height = 45.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Image(
                                painterResource(id = R.drawable.iconmap),
                                contentDescription = "Map Icon",
                            )
                        }

                    }

                    Button(
                        onClick = {
                            createButtonClick = true

                        },
                        enabled = !(roomName.isNullOrEmpty() || exerciseSelect.isNullOrEmpty() || genderSelect.isNullOrEmpty() || timeStart == "None" || timeEnd == "None" || numberMember.isNullOrEmpty() || placeSelect.isNullOrEmpty() || lat == null || lng == null),
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