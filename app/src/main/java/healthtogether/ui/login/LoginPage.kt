package healthtogether.ui.login

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.components.AppBackground
import healthtogether.components.AppButton
import healthtogether.components.AppColor
import healthtogether.components.ApplicationNameText
import healthtogether.components.AuthScreen
import healthtogether.components.TextBottomVersion
import healthtogether.components.TextClickAble
import healthtogether.components.TextField
import healthtogether.components.TextFieldPassword
import healthtogether.components.TextHeader
import healthtogether.ui.AppTheme
import healthtogether.ui.home.HomeActivity
import healthtogether.ui.login.loginmodel.LoginPageViewModel

@Composable
fun LoginView(viewModel: LoginPageViewModel, navController: NavController) = AppTheme {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val versionApp = "0.0.1"
    val token by viewModel.token.observeAsState()

    LaunchedEffect(token) {
        if (token != null) {
            if (token!!.isAuthenticated == true) {
                val intent = Intent(context, HomeActivity::class.java)
                intent.putExtra("tokenAuth", token!!.tokenAuth)
                intent.putExtra("isAuthenticated", token!!.isAuthenticated)
                intent.putExtra("userName", token!!.userName)
                intent.putExtra("userStatus", token!!.userStatus)
                intent.putExtra("chatId", token!!.chatId)
                intent.putExtra("userId",token!!.userId)
                context.startActivity(intent)
            }
        }
    }

    Surface(
        Modifier.fillMaxSize(1f),
    ) {
        AppBackground()
        Column(
            verticalArrangement = Arrangement.Bottom,
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
                    .padding(bottom = 20.dp)
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
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        TextHeader("Welcome")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                "Username",
                                email,
                                onChangeValue = { email = it },
                                Icons.Default.Email,
                                focusManager
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            TextFieldPassword(
                                "Password",
                                password,
                                onChangeValue = { password = it },
                                Icons.Default.Lock,
                                focusManager
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 17.dp)
                        ) {
                            AppButton("Login") {
                                if (!(email.isEmpty() || password.isEmpty())) {
                                    viewModel.login(email, password)
                                }
                            }
                        }

                        Row(modifier = Modifier.padding(top = 30.dp)) {
                            Image(painter = painterResource(id = R.drawable.face_scan),
                                contentDescription = "Face Scan",
                                modifier = Modifier
                                    .padding(horizontal = 60.dp, vertical = 0.dp)
                                    .width(40.dp)
                                    .height(40.dp)
                                    .clickable { navController.navigate(AuthScreen.FaceScanScreen.route) }
                            )
                        }

                        Row {
                            TextClickAble(
                                value = "Register",
                                30,
                                10,
                                navController,
                                AuthScreen.RegisterScreen.route
                            )
                        }

                        Row {
                            Text(
                                "or",
                                fontWeight = FontWeight.W600,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                color = AppColor.Primary
                            )
                        }

                        Row {
                            TextClickAble(
                                value = "Forget Password",
                                10,
                                30,
                                navController,
                                AuthScreen.ForgotPasswordScreen.route
                            )
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 0.dp, end = 30.dp)
            ) {
                TextBottomVersion(versionApp)
            }
        }
    }
}