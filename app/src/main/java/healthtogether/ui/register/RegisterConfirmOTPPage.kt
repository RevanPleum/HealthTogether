package healthtogether.ui.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import healthtogether.apiService.model.Accounts.RequestConfirmEmailViewModel
import healthtogether.apiService.model.Accounts.ResponseCreateUserViewModel
import healthtogether.components.AppBackground
import healthtogether.components.AppButton
import healthtogether.components.ApplicationNameText
import healthtogether.components.AuthScreen
import healthtogether.components.TextHeaderBoxScope
import healthtogether.components.TextOnly
import healthtogether.components.TimeFormatExt.timeFormat
import healthtogether.ui.AppTheme
import healthtogether.ui.register.registermodel.ConfirmOTPViewModel

@Composable
fun RegisterConfirmOTPPage(
    viewModel: ConfirmOTPViewModel,
    data: String?,
    navController: NavController
) = AppTheme {
    val focusManager = LocalFocusManager.current
//    val context = LocalContext.current
//    var isResetTime by rememberSaveable { mutableStateOf(false) }
    var refCode by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    val isConfirmSuccess by viewModel.isConfirmSuccess.observeAsState()
    val responseChatSuccess by viewModel.responseChatSuccess.observeAsState()
    val isChatIdSuccess by viewModel.isChatIdSuccess.observeAsState()
    var otpList = listOf(
        rememberSaveable { mutableStateOf("") },
        rememberSaveable { mutableStateOf("") },
        rememberSaveable { mutableStateOf("") },
        rememberSaveable { mutableStateOf("") },
//        rememberSaveable {mutableStateOf("")},
//        rememberSaveable {mutableStateOf("")},
    )

    val requesterList = listOf(
        FocusRequester(),
        FocusRequester(),
        FocusRequester(),
        FocusRequester(),
//        FocusRequester(),
//        FocusRequester()
    )

    LaunchedEffect(Unit) {
        val data = data?.let {
            Gson().fromJson(it, ResponseCreateUserViewModel::class.java)
        }
        viewModel.UpdateData(data!!)
        refCode = viewModel.tokenAndOTP.value?.refOTP!!
        username = viewModel.tokenAndOTP.value?.userName!!
        viewModel.startCountDownTimer()
    }

    LaunchedEffect(isConfirmSuccess) {
        if (isConfirmSuccess == true) {
            navController.navigate(AuthScreen.RegisterCompletedScreen.route)
//            viewModel.RegistrationChat(username)
        }
    }

//    LaunchedEffect(responseChatSuccess) {
//        if (responseChatSuccess?.isSuccess == true) {
//            viewModel.saveChatId(RequestChatIdViewModel(responseChatSuccess?.userName, responseChatSuccess?.chatId))
//        }
//    }
//
//    LaunchedEffect(isChatIdSuccess) {
//        if (isChatIdSuccess == true) {
//            navController.navigate(AuthScreen.RegisterCompletedScreen.route)
//        }
//    }

    Surface(
        Modifier.fillMaxSize(1f),
    ) {
        AppBackground()
        Column(
//            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .clickable { focusManager.clearFocus() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 50.dp)
            ) {
                ApplicationNameText()
            }
            Row {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, bottom = 150.dp),
                    elevation = 5.dp,
                    border = BorderStroke(0.dp, Color.White),
                    shape = RoundedCornerShape(15.dp),

                    ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                TextHeaderBoxScope(
                                    "Confirm OTP",
                                    Modifier
                                        .align(Alignment.Center)
                                        .padding(horizontal = 20.dp, vertical = 20.dp)
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
                        ) {
                            TextOnly(value = "Ref. OTP: ", weightsize = FontWeight.W200)
                            TextOnly(value = refCode, weightsize = FontWeight.W500)
//                            for(item in otpList)
//                            {
//                                TextOnly(value = item.value, weightsize = FontWeight.W500)
//                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        ) {
                            TextOnly(value = "Time out: ", weightsize = FontWeight.W200)
                            viewModel.apply {
                                TextOnly(
                                    value = timeLeft.value.timeFormat(),
                                    weightsize = FontWeight.W500
                                )
                            }

                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        ) {
                            TextOnly(
                                value = "Please enter the OTP which\nwe have sent to your email recently",
                                weightsize = FontWeight.W200
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        ) {
                            OTPBox(otpList, requesterList)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        ) {
//                            TextClickAble(
//                                value = "Resend",
//                                5,
//                                5
//                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 60.dp, bottom = 30.dp)
                        ) {
                            AppButton("Confirm") {
                                val confirmOTP = RequestConfirmEmailViewModel(
                                    userName = username,
                                    otp = otpList.joinToString("") { it.value },
                                    ref = refCode
                                )
                                viewModel.comfirmEmail(confirmOTP)
                            }
                        }
                    }
                }
            }
        }
    }
}