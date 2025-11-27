package healthtogether.components


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.CountDownTimer
import android.util.Base64
import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.heathtogethermobile.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import healthtogether.apiService.model.Dropdowns.BaseDropdownViewModel
import healthtogether.apiService.model.Dropdowns.DropdownBodyViewModel
import healthtogether.apiService.service.DropdownService
import healthtogether.components.TimeFormatExt.timeFormat
import healthtogether.ui.home.homemodel.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Composable
fun AppBackground() {
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = "app_background",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ApplicationNameText() {
    Text(
        "Health Together",
        fontWeight = FontWeight.W900,
        fontSize = AppFontSize.ApplicationName,
        textAlign = TextAlign.Center,
        color = AppColor.Primary,
        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
    )
}

@Composable
fun TextHeader(value: String) {
    Text(
        text = value,
        fontWeight = FontWeight.W800,
        fontSize = AppFontSize.Header,
        textAlign = TextAlign.Center,
        color = AppColor.Primary,
        modifier = Modifier.padding(top = 15.dp)
    )
}

@Composable
fun TextHeaderBoxScope(value: String, modifier: Modifier) {
    Text(
        text = value,
        fontWeight = FontWeight.W700,
        fontSize = AppFontSize.Header,
        textAlign = TextAlign.Center,
        color = AppColor.Primary,
        modifier = modifier
    )
}

@Composable
fun NumberField(
    labelValue: String,
    value: String,
    onChangeValue: (String) -> Unit,
    focusManager: FocusManager
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        shape = RoundedCornerShape(15.dp),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = AppColor.BorderGrey,
            focusedLabelColor = AppColor.BorderGrey,
            cursorColor = AppColor.BorderGrey,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        value = value.toString(),
        onValueChange = onChangeValue,
    )
}

@Composable
fun TextBottomVersion(value: String) {
    Text(
        "version : $value  ",
        fontWeight = FontWeight.W300,
        fontSize = AppFontSize.Version,
        textAlign = TextAlign.Right,
        color = AppColor.Primary
    )
}

@Composable
fun TextField(
    labelValue: String,
    value: String,
    onChangeValue: (String) -> Unit,
    focusManager: FocusManager
) {

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        shape = RoundedCornerShape(15.dp),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = AppColor.BorderGrey,
            focusedLabelColor = AppColor.BorderGrey,
            cursorColor = AppColor.BorderGrey,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        value = value,
        onValueChange = onChangeValue,
    )
}

@Composable
fun TextField(
    labelValue: String,
    value: String,
    onChangeValue: (String) -> Unit,
    icon: ImageVector,
    focusManager: FocusManager
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        shape = RoundedCornerShape(15.dp),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = AppColor.BorderGrey,
            focusedLabelColor = AppColor.BorderGrey,
            cursorColor = AppColor.BorderGrey,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),

        value = value,
        onValueChange = onChangeValue,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = ""
            )
        }
    )
}

@Composable
fun TextFieldPassword(
    labelValue: String,
    value: String,
    onChangeValue: (String) -> Unit,
    icon: ImageVector,
    focusManager: FocusManager
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        shape = RoundedCornerShape(15.dp),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = AppColor.BorderGrey,
            focusedLabelColor = AppColor.BorderGrey,
            cursorColor = AppColor.BorderGrey,
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        value = value,
        onValueChange = onChangeValue,
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = ""
            )
        }
    )
}

@Composable
fun TextFieldPassword(
    labelValue: String,
    value: String,
    onChangeValue: (String) -> Unit,
    focusManager: FocusManager
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        shape = RoundedCornerShape(15.dp),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = AppColor.BorderGrey,
            focusedLabelColor = AppColor.BorderGrey,
            cursorColor = AppColor.BorderGrey,
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        value = value,
        onValueChange = onChangeValue,
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        }
    )
}

@Composable
fun TextClickAble(
    value: String,
    topvalue: Int,
    bottomvalue: Int,
    navController: NavController,
    nameScreen: String
) {
    Text(
        "$value",
        fontWeight = FontWeight.W500,
        fontSize = AppFontSize.Version,
        textAlign = TextAlign.Center,
        color = AppColor.Primary,
        modifier = Modifier
            .padding(top = topvalue.dp, bottom = bottomvalue.dp)
            .clickable {
                navController.navigate(nameScreen)
            },
        style = TextStyle(textDecoration = TextDecoration.Underline),

        )
}

@Composable
fun TextOnly(value: String, weightsize: FontWeight = FontWeight.W100) {
    Text(
        "$value",
        fontWeight = weightsize,
        fontSize = AppFontSize.Version,
        textAlign = TextAlign.Center,
        color = AppColor.Primary,
    )
}

@Composable
fun TextOnly(
    value: String,
    topvalue: Int,
    bottomvalue: Int,
    fontSize: TextUnit = AppFontSize.Version,
    weightsize: FontWeight = FontWeight.W100
) {
    Text(
        "$value",
        fontWeight = weightsize,
        fontSize = fontSize,
        textAlign = TextAlign.Center,
        color = AppColor.Primary,
        modifier = Modifier
            .padding(top = topvalue.dp, bottom = bottomvalue.dp)
    )
}


@Composable
fun AppButton(value: String, onClick: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppColor.Primary
        ),
        shape = RoundedCornerShape(30),
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .height(50.dp),
        onClick = onClick
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W700,
            color = Color.White
        )
    }
}

@Composable
fun CategoryButton(buttonName: String, imageId: Int, intent: Intent, context: Context) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "app_background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 10.dp)
                .size(150.dp)
                .clickable {
                    context.startActivity(intent)
                },
        )
        Text(
            text = buttonName,
            fontFamily = FontFamily(Font(R.font.segoeuib)),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF0F7B2C),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 0.dp)
                .align(Alignment.Center)
                .background(
                    color = Color.White.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(size = 10.dp)
                )
        )
    }
}

@Composable
fun CategoryNavigateButton(
    buttonName: String,
    imageId: Int,
    screenRoute: String,
    navController: NavController,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "app_background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 10.dp)
                .size(180.dp)
                .clickable {
                    navController.navigate(screenRoute)
                },
        )
        Text(
            text = buttonName,
            fontFamily = FontFamily(Font(R.font.segoeuib)),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF0F7B2C),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 0.dp)
                .align(Alignment.Center)
                .background(
                    color = Color.White.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(size = 10.dp)
                )
        )
    }
}

@Composable
fun dropDownMenu(
    labelValue: String,
    list: List<BaseDropdownViewModel>,
    onOptionSelected: (BaseDropdownViewModel) -> Unit
) {
    var defaultDropdowList = BaseDropdownViewModel()
    var expanded by remember { mutableStateOf(false) }
    var selectOption by remember { mutableStateOf(defaultDropdowList) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
        OutlinedTextField(
            value = selectOption.text,
            onValueChange = {},
            readOnly = true,
            label = { Text(labelValue) },
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = AppColor.BorderGrey,
                focusedLabelColor = AppColor.BorderGrey,
                cursorColor = AppColor.BorderGrey,
            ),
            trailingIcon = {
                Icon(icon, "contentDescription")
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .clickable { expanded = true },
            enabled = false
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            list.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectOption = item
                    expanded = false
                    onOptionSelected(item)
                }) {
                    Text(text = item.text)
                }
            }
        }
    }
}

fun isValidEmail(email: String): Boolean {
//    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color)
    }
}

@Composable
fun TopAppNameAndProfile(navController: NavController, imageProfile: String) {

    val imageBytes = Base64.decode(imageProfile, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(
        imageBytes,
        0,
        imageBytes.size
    )
    val painter = rememberAsyncImagePainter(
        if (imageProfile == "") R.drawable.profile else decodedImage
    )
    Card(
        backgroundColor = Color.White,
        elevation = 5.dp,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 20.dp,
            bottomStart = 0.dp,
            bottomEnd = 20.dp
        ),
        modifier = Modifier
            .size(width = 230.dp, height = 50.dp)

    ) {
        TextHeaderBoxScope(
            value = "Health Together",
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
    }

    Image(
        painter = painter,
        contentDescription = "Profile Picture",
        modifier = Modifier
            .padding(start = 80.dp)
            .size(70.dp)
            .clip(CircleShape)
            .border(
                BorderStroke(1.dp, Color.White),
                CircleShape
            )
            .clickable { navController.navigate(HomeScreen.ProfileScreen.route) },
        contentScale = ContentScale.Crop,
    )

}

@Composable
fun BottomMenu(state: HomeViewState, navController: NavController) {
    Card(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(),
        elevation = 10.dp, border = BorderStroke(1.dp, Color.White),
        shape = RoundedCornerShape(
            topStart = 30.dp,
            topEnd = 30.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        ),

        ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Image(painterResource(id = if (navController.currentDestination?.route == HomeScreen.FriendListScreen.route) R.drawable.iconfriendselect else R.drawable.iconfriend),
                contentDescription = "Friend Icon",
                modifier = Modifier.clickable {
                    navController.navigate(HomeScreen.FriendListScreen.route) {

                    }
                })

            Image(painterResource(id = if (navController.currentDestination?.route == HomeScreen.ChatListScreen.route) R.drawable.icon_chat_select else R.drawable.icon_chat),
                contentDescription = "Chat Icon",
                modifier = Modifier.clickable {
                    navController.navigate(HomeScreen.ChatListScreen.route) {

                    }
                })

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                ),
                modifier = Modifier.fillMaxHeight(),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    navController.navigate(HomeScreen.MainHomeScreen.route) {

                    }
                },
            ) {
                Image(
                    painterResource(id = if (navController.currentDestination?.route == HomeScreen.MainHomeScreen.route) R.drawable.iconexcersiceselect else R.drawable.iconexcersice),
                    contentDescription = "Exercise Icon",
                    contentScale = ContentScale.Crop
                )
            }

            Image(painterResource(id = if (navController.currentDestination?.route == HomeScreen.HistoryScreen.route) R.drawable.iconhistoryselect else R.drawable.iconhistory2),
                contentDescription = "History Icon",
                modifier = Modifier.clickable {
                    navController.navigate(HomeScreen.HistoryScreen.route) {

                    }
                })

            Image(painterResource(id = if (navController.currentDestination?.parent?.route == SettingScreen.Setiing.route) R.drawable.iconsettingselect else R.drawable.iconsetting),
                contentDescription = "Setting Icon",
                modifier = Modifier.clickable {
                    navController.navigate(SettingScreen.MainSettingScreen.route) {

                    }
                })
        }
    }
}

@Composable
fun ShowLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = AppColor.Primary,
            strokeWidth = 10.dp,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
        )
        Image(
            painterResource(id = R.drawable.iconexcersiceselect),
            contentDescription = "AppIconLoading",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


object TimeFormatExt {
    private const val FORMAT = "%02d:%02d"

    fun Long.timeFormat(): String = String.format(
        FORMAT,
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )
}

@Composable
fun Timer() {

    val userInputMinute = TimeUnit.MINUTES.toMillis(10)
    val userInputSecond = TimeUnit.SECONDS.toMillis(30)

    val initialTotalTimeInMillis = userInputMinute + userInputSecond

    var timeLeft by rememberSaveable { mutableStateOf(initialTotalTimeInMillis) }
    val countDownInterval = 1000L // 1 seconds is the lowest


    val isPlaying = rememberSaveable {
        mutableStateOf(false)
    }

    val countDownTimer = object : CountDownTimer(timeLeft, countDownInterval) {
        override fun onTick(currentTimeLeft: Long) {
            timeLeft = currentTimeLeft
        }

        override fun onFinish() {
            isPlaying.value = false
        }
    }

    DisposableEffect(key1 = "key") {
        countDownTimer.start()
        onDispose {
            countDownTimer.cancel()
        }
    }

    Text(
        text = timeLeft.timeFormat()
    )
}

@Composable
fun MyAppButton(task: () -> Unit, buttonName: String) {
    androidx.compose.material3.Button(onClick = task) {
        androidx.compose.material3.Text(text = buttonName)
    }
}


suspend fun getProvinceList(language: String): List<BaseDropdownViewModel> {

    return withContext(Dispatchers.IO) {
        val response = DropdownService.dropdown_API.province(language)
        if (response.isSuccess == true) {
            response.data!!
        } else {
            throw Exception("Query Failed: ${response.message}")
        }
    }
}

suspend fun getDistrictList(provinceID: String, language: String): List<BaseDropdownViewModel> {

    return withContext(Dispatchers.IO) {
        val response = DropdownService.dropdown_API.district(provinceID,language)
        if (response.isSuccess == true) {
            response.data!!
        } else {
            throw Exception("Query Failed: ${response.message}")
        }
    }
}

suspend fun getSubDistrictList(
    districtID: String,
    language: String
): List<BaseDropdownViewModel> {

    return withContext(Dispatchers.IO) {
        val response = DropdownService.dropdown_API.subDistrict(districtID,language)
        if (response.isSuccess == true) {
            response.data!!
        } else {
            throw Exception("Query Failed: ${response.message}")
        }
    }
}

suspend fun getPlace(
    provinceID: String,
    districtID: String,
    subDistrictID: String,
    language: String
): List<BaseDropdownViewModel> {

    return withContext(Dispatchers.IO) {
        val requestBody = DropdownBodyViewModel(provinceID,districtID,subDistrictID)
        val response = DropdownService.dropdown_API.place(requestBody,language)
        if (response.isSuccess == true) {
            response.data!!
        } else {
            throw Exception("Query Failed: ${response.message}")
        }
    }
}

suspend fun getExerciseList(
    language: String
): List<BaseDropdownViewModel> {

    return withContext(Dispatchers.IO) {
        val response = DropdownService.dropdown_API.exercise(language)
        if (response.isSuccess == true) {
            response.data!!
        } else {
            throw Exception("Query Failed: ${response.message}")
        }
    }
}


suspend fun getGenderList(
    language: String
): List<BaseDropdownViewModel> {

    return withContext(Dispatchers.IO) {
        val response = DropdownService.dropdown_API.gender(language)
        if (response.isSuccess == true) {
            response.data!!
        } else {
            throw Exception("Query Failed: ${response.message}")
        }
    }
}


fun encodeImage(context: Context,   image: Uri,  quality: Int):String{
    val input =
        context.contentResolver.openInputStream(image)
    val bm = BitmapFactory.decodeStream(input)
    val baos = ByteArrayOutputStream()
    bm.compress(
        Bitmap.CompressFormat.JPEG,
        quality,
        baos
    ) //bm is the bitmap object
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}