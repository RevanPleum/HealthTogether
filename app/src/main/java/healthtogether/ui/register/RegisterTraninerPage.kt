package healthtogether.ui.register

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.apiService.model.Dropdowns.BaseDropdownViewModel
import healthtogether.components.AppButton
import healthtogether.components.AppColor
import healthtogether.components.AppFontSize
import healthtogether.components.ApplicationNameText
import healthtogether.components.AuthScreen
import healthtogether.components.NumberField
import healthtogether.components.TextClickAble
import healthtogether.components.TextField
import healthtogether.components.TextFieldPassword
import healthtogether.components.TextHeaderBoxScope
import healthtogether.components.TextOnly
import healthtogether.components.dropDownMenu
import healthtogether.components.isValidEmail
import healthtogether.ui.AppTheme
import healthtogether.ui.register.registermodel.RegisterViewModel

@Composable
fun RegisterTrainerFormView(navController: NavController) = AppTheme {
    val focusManager = LocalFocusManager.current
//    val systemUiController = rememberSystemUiController()
//    systemUiController.isStatusBarVisible = false // Status bar
//    systemUiController.isNavigationBarVisible = false // Navigation bar

    Box {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "app_background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
//                        systemUiController.isStatusBarVisible = false // Status bar
//                        systemUiController.isNavigationBarVisible = false // Navigation bar
                    })
                },
        ) {
            RegisterTrainerFields(RegisterViewModel(), navController)
        }
    }


}

@Composable
fun RegisterTrainerFields(
    viewModel: RegisterViewModel,
    navController: NavController
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var firstname by rememberSaveable { mutableStateOf("") }
    var lastname by rememberSaveable { mutableStateOf("") }
    var idCard by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var bitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }

    var launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    var genderList by rememberSaveable { mutableStateOf(listOf<BaseDropdownViewModel>()) }
    var exerciseList by rememberSaveable { mutableStateOf(listOf<BaseDropdownViewModel>()) }
    var careerList by rememberSaveable { mutableStateOf(listOf<BaseDropdownViewModel>()) }

    LaunchedEffect(Unit) {
        genderList = viewModel.getGenders()!!
        exerciseList = viewModel.getExercises()!!
        careerList = viewModel.getCareers()!!
    }

    fun isValidData(): Boolean {
        return true
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),

        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ApplicationNameText()

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            elevation = 20.dp,
            border = BorderStroke(0.dp, Color.White),
            shape = RoundedCornerShape(15.dp),

            ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),

                ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.iconback),
                            contentDescription = "BackIcon",
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 40.dp)
                                .clickable {
                                    navController.navigate(AuthScreen.RegisterScreen.route) {
                                        popUpTo(AuthScreen.Login.route) {
                                            inclusive = true
                                        }
                                    }
                                },
                        )

                        TextHeaderBoxScope(
                            "Register\nTrainer",
                            Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 20.dp, vertical = 20.dp)
                        )
                    }
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item("register_trainer_email")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "Username",
                                email,
                                onChangeValue = { email = it },
                                Icons.Default.Email,
                                focusManager
                            )
                        }
                        if (!email.isEmpty() && !isValidEmail(email)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "*Email invalid",
                                    color = AppColor.Danger,
                                    modifier = Modifier.padding(start = 15.dp)
                                )
                            }
                        }
                    }

                    item("register_trainer_password")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextFieldPassword(
                                "Password",
                                password,
                                onChangeValue = { password = it },
                                Icons.Default.Lock,
                                focusManager
                            )
                        }
                    }

                    item("register_trainer_confirm_password")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextFieldPassword(
                                "Confirm Password",
                                confirmPassword,
                                onChangeValue = { confirmPassword = it },
                                Icons.Default.Lock,
                                focusManager
                            )
                        }
                    }

                    item("register_trainer_age")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            NumberField(
                                "Age",
                                age,
                                onChangeValue = {
                                    if (it.isEmpty() || it.toInt() < 120) age = it
                                },
                                focusManager
                            )
                        }
                    }

                    item("register_trainer_gender")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            dropDownMenu(
                                "Gender",
                                genderList,
                                onOptionSelected = {

                                }
                            )
                        }
                    }

                    item("register_trainer_career")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            dropDownMenu(
                                "Career",
                                careerList,
                                onOptionSelected = {

                                }
                            )
                        }
                    }

                    item("register_trainer_favorite_exercise")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            dropDownMenu(
                                "Favorite Exercise",
                                exerciseList,
                                onOptionSelected = {

                                }
                            )
                        }
                    }

                    item("register_trainer_firstname")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "First Name",
                                firstname,
                                onChangeValue = { firstname = it },
                                focusManager
                            )
                        }
                    }

                    item("register_trainer_lastname")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "Last Name",
                                lastname,
                                onChangeValue = { lastname = it },
                                focusManager
                            )
                        }
                    }

                    item("register_trainer_id_card")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "Citizen Id",
                                idCard,
                                onChangeValue = { idCard = it },
                                focusManager
                            )
                        }
                    }
                    item("register_trainer_file")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            imageUri?.let {
                                if (Build.VERSION.SDK_INT < 28) {
                                    bitmap = MediaStore.Images.Media.getBitmap(
                                        context.contentResolver,
                                        it
                                    )
                                } else {
                                    val source =
                                        ImageDecoder.createSource(context.contentResolver, it)
                                    bitmap = ImageDecoder.decodeBitmap(source)
                                }

                                bitmap?.let { btm ->
                                    Image(
                                        bitmap = btm.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(400.dp)
                                            .padding(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            AppButton("Upload Certificate") {
                                launcher.launch("image/*")
                            }
                        }
                    }
                    item("register_trainer_button")
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            TextOnly(
                                "By opening a Health Together account,",
                                5,
                                5,
                                AppFontSize.Version,
                                FontWeight.W500
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            TextOnly(
                                "you acknowledge and agree to ",
                                5,
                                5,
                                AppFontSize.Version,
                                FontWeight.W500
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            TextClickAble(
                                value = "Terms",
                                5,
                                5,
                                navController,
                                AuthScreen.RegisterScreen.route
                            )
                            TextOnly(" and ", 5, 5, AppFontSize.Version, FontWeight.W500)
                            TextClickAble(
                                value = "Conditions of use",
                                5,
                                5,
                                navController,
                                AuthScreen.RegisterScreen.route
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 17.dp, bottom = 15.dp)
                        ) {
                            AppButton("Next") {
                                if (isValidData()) {
                                    navController.navigate(AuthScreen.RegisterConfirmOTPScreen.route)
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
fun UpgradeToTrainerView(
    viewModel: RegisterViewModel,
    navController: NavController
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var age by rememberSaveable { mutableStateOf("") }
    var firstname by rememberSaveable { mutableStateOf("") }
    var lastname by rememberSaveable { mutableStateOf("") }
    var idCard by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var bitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }

    var launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    var genderList by rememberSaveable { mutableStateOf(listOf<BaseDropdownViewModel>()) }
    var exerciseList by rememberSaveable { mutableStateOf(listOf<BaseDropdownViewModel>()) }
    var careerList by rememberSaveable { mutableStateOf(listOf<BaseDropdownViewModel>()) }

    LaunchedEffect(Unit) {
        genderList = viewModel.getGenders()!!
        exerciseList = viewModel.getExercises()!!
        careerList = viewModel.getCareers()!!
    }

    fun isValidData(): Boolean {
        return true
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),

        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ApplicationNameText()

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            elevation = 20.dp,
            border = BorderStroke(0.dp, Color.White),
            shape = RoundedCornerShape(15.dp),

            ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),

                ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.iconback),
                            contentDescription = "BackIcon",
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 40.dp)
                                .clickable {
                                    navController.popBackStack()
                                },
                        )

                        TextHeaderBoxScope(
                            "Register\nTrainer",
                            Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 20.dp, vertical = 20.dp)
                        )
                    }
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {

                    item("register_trainer_age")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            NumberField(
                                "Age",
                                age,
                                onChangeValue = {
                                    if (it.isEmpty() || it.toInt() < 120) age = it
                                },
                                focusManager
                            )
                        }
                    }

                    item("register_trainer_gender")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            dropDownMenu(
                                "Gender",
                                genderList,
                                onOptionSelected = {

                                }
                            )
                        }
                    }

                    item("register_trainer_career")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            dropDownMenu(
                                "Career",
                                careerList,
                                onOptionSelected = {

                                }
                            )
                        }
                    }

                    item("register_trainer_favorite_exercise")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            dropDownMenu(
                                "Favorite Exercise",
                                exerciseList,
                                onOptionSelected = {

                                }
                            )
                        }
                    }

                    item("register_trainer_firstname")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "First Name",
                                firstname,
                                onChangeValue = { firstname = it },
                                focusManager
                            )
                        }
                    }

                    item("register_trainer_lastname")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "Last Name",
                                lastname,
                                onChangeValue = { lastname = it },
                                focusManager
                            )
                        }
                    }

                    item("register_trainer_id_card")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "Citizen Id",
                                idCard,
                                onChangeValue = { idCard = it },
                                focusManager
                            )
                        }
                    }
                    item("register_trainer_file")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            imageUri?.let {
                                if (Build.VERSION.SDK_INT < 28) {
                                    bitmap = MediaStore.Images.Media.getBitmap(
                                        context.contentResolver,
                                        it
                                    )
                                } else {
                                    val source =
                                        ImageDecoder.createSource(context.contentResolver, it)
                                    bitmap = ImageDecoder.decodeBitmap(source)
                                }

                                bitmap?.let { btm ->
                                    Image(
                                        bitmap = btm.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(400.dp)
                                            .padding(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            AppButton("Upload Certificate") {
                                launcher.launch("image/*")
                            }
                        }
                    }
                    item("register_trainer_button")
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            TextOnly(
                                "By opening a Health Together account,",
                                5,
                                5,
                                AppFontSize.Version,
                                FontWeight.W500
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            TextOnly(
                                "you acknowledge and agree to ",
                                5,
                                5,
                                AppFontSize.Version,
                                FontWeight.W500
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            TextClickAble(
                                value = "Terms",
                                5,
                                5,
                                navController,
                                AuthScreen.RegisterScreen.route
                            )
                            TextOnly(" and ", 5, 5, AppFontSize.Version, FontWeight.W500)
                            TextClickAble(
                                value = "Conditions of use",
                                5,
                                5,
                                navController,
                                AuthScreen.RegisterScreen.route
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 17.dp, bottom = 15.dp)
                        ) {
                            AppButton("Next") {
                                if (isValidData()) {
                                    navController.navigate(AuthScreen.RegisterConfirmOTPScreen.route)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}