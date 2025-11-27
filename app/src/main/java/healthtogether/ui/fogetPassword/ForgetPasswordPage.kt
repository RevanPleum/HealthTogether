package healthtogether.ui.fogetPassword

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.components.AppBackground
import healthtogether.components.AppButton
import healthtogether.components.AppColor
import healthtogether.components.ApplicationNameText
import healthtogether.components.AuthScreen
import healthtogether.components.TextField
import healthtogether.components.TextHeaderBoxScope
import healthtogether.components.isValidEmail
import healthtogether.ui.AppTheme
import healthtogether.ui.fogetPassword.forgetPasswordViewModel.ForgetPasswordViewModel


@Composable
fun ForgetPassword(viewModel: ForgetPasswordViewModel, navController: NavController) = AppTheme {
    var email by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val versionApp = "0.0.1"
    val isSendSuccess by viewModel.isSendSuccess.observeAsState()
    LaunchedEffect(isSendSuccess) {
        if (isSendSuccess != null) {
            if (isSendSuccess!! == true) {
                navController.navigate(AuthScreen.SendPasswordScreen.route)
            }
        }
    }
    Surface(
        Modifier.fillMaxSize(1f),
    ) {
        AppBackground()
        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
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
                        .padding(start = 30.dp, end = 30.dp),
                    elevation = 5.dp,
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
                                            navController.navigate(AuthScreen.Auth.route) {
                                                popUpTo(AuthScreen.Auth.route) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                )
                                TextHeaderBoxScope(
                                    "Forget Password",
                                    Modifier
                                        .align(Alignment.Center)
                                        .padding(horizontal = 20.dp, vertical = 20.dp)
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "Email",
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

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
                        ) {
                            AppButton("Send") {
                                if (!email.isEmpty() && isValidEmail(email)) {
                                    viewModel.forgetPassword(email)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
