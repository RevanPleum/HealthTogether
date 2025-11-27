package healthtogether.ui.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import com.google.gson.Gson
import healthtogether.apiService.model.Accounts.RequestCreateUserViewModel
import healthtogether.apiService.model.Dropdowns.BaseDropdownViewModel
import healthtogether.components.AppButton
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
import healthtogether.ui.AppTheme
import healthtogether.ui.register.registermodel.RegisterViewModel

@Composable
fun RegisterMemberFormView(navController: NavController) = AppTheme {
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
            RegisterMemberFields(RegisterViewModel(), navController)
        }
    }


}

@Composable
fun RegisterMemberFields(
    viewModel: RegisterViewModel,
    navController: NavController
) {
    val focusManager = LocalFocusManager.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var career by rememberSaveable { mutableStateOf("") }
    var exercise by rememberSaveable { mutableStateOf("") }
    var genderList by rememberSaveable { mutableStateOf(listOf<BaseDropdownViewModel>()) }
    var exerciseList by rememberSaveable { mutableStateOf(listOf<BaseDropdownViewModel>()) }
    var careerList by rememberSaveable { mutableStateOf(listOf<BaseDropdownViewModel>()) }
    val tokenAndOTP by viewModel.tokenAndOTP.observeAsState()
    LaunchedEffect(Unit) {
        genderList = viewModel.getGenders()!!
        exerciseList = viewModel.getExercises()!!
        careerList = viewModel.getCareers()!!
    }

    LaunchedEffect(tokenAndOTP) {
        if (tokenAndOTP != null) {
            if (tokenAndOTP!!.isAuthenticated == true) {
                val dataJson = Gson().toJson(tokenAndOTP)
                navController.navigate(
                    AuthScreen.RegisterConfirmOTPScreen.route.replace(
                        "{data}",
                        dataJson
                    )
                )
            }
        }
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
                horizontalAlignment = Alignment.CenterHorizontally,
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
                            "Register\nMember",
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
                    item("register_member_email")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "Username*",
                                email,
                                onChangeValue = { email = it },
                                Icons.Default.Email,
                                focusManager
                            )
                        }
//                        if (!email.isEmpty() && !isValidEmail(email)) {
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Text(
//                                    text = "*Email invalid",
//                                    color = AppColor.Danger,
//                                    modifier = Modifier.padding(start = 15.dp)@
//                                )
//                            }
//                        }
                    }

                    item("register_member_password")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextFieldPassword(
                                "Password*",
                                password,
                                onChangeValue = { password = it },
                                Icons.Default.Lock,
                                focusManager
                            )
                        }
                    }

                    item("register_member_confirm_password")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextFieldPassword(
                                "Confirm Password*",
                                confirmPassword,
                                onChangeValue = { confirmPassword = it },
                                Icons.Default.Lock,
                                focusManager
                            )
                        }
                    }

                    item("register_member_age")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            NumberField(
                                "Age*",
                                age,
                                onChangeValue = {
                                    if (it.isEmpty() || it.toInt() < 120) age = it
                                },
                                focusManager
                            )
                        }
                    }

                    item("register_member_gender")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            dropDownMenu(
                                "Gender",
                                genderList,
                                onOptionSelected = {
                                    gender = it.value
                                }
                            )
                        }
                    }

                    item("register_member_career")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            dropDownMenu(
                                "Career",
                                careerList,
                                onOptionSelected = {
                                    career = it.value
                                }
                            )
                        }
                    }

                    item("register_member_favorite_exercise")
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            dropDownMenu(
                                "Favorite Exercise",
                                exerciseList,
                                onOptionSelected = {
                                    exercise = it.value
                                }
                            )
                        }
                    }

                    item("register_member_button")
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
                                    val createUser = RequestCreateUserViewModel(
                                        userName = email,
                                        name = email,
                                        password = password,
                                        confirmPassword = confirmPassword,
                                        gender = gender,
                                        age = age,
                                        exerciseId = exercise,
                                        careerId = career,
                                        category = "MEMBER"
                                    )
                                    viewModel.createUser(createUser)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}