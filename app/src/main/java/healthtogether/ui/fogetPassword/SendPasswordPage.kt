package healthtogether.ui.fogetPassword

import android.content.Intent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import healthtogether.components.AppBackground
import healthtogether.components.AppButton
import healthtogether.components.ApplicationNameText
import healthtogether.components.AuthScreen
import healthtogether.components.TextHeaderBoxScope
import healthtogether.components.TextOnly
import healthtogether.ui.AppTheme
import healthtogether.ui.MainActivity


@Composable
fun SendPasswordPage(navController: NavController) = AppTheme {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val intent = Intent(context, MainActivity::class.java)
    Surface(
        Modifier.fillMaxSize(1f),
    ) {
        AppBackground()
        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
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
                        .padding(start = 30.dp, end = 30.dp),
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
                                    "Notification",
                                    Modifier
                                        .align(Alignment.Center)
                                        .padding(horizontal = 20.dp, vertical = 20.dp)
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
                        ) {
                            TextOnly(value = "Send password to email complete")
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
                        ) {
                            AppButton("Close") {
                                navController.navigate(AuthScreen.Auth.route) {
                                    popUpTo(AuthScreen.Auth.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}