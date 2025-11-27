package healthtogether.ui.register

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.components.ApplicationNameText
import healthtogether.components.AuthScreen
import healthtogether.components.CategoryNavigateButton
import healthtogether.components.TextHeaderBoxScope
import healthtogether.ui.AppTheme
import healthtogether.ui.register.registermodel.RegisterViewModel

@Composable
fun RegisterView(viewModel: RegisterViewModel, navController: NavController) = AppTheme {
    val focusManager = LocalFocusManager.current

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
                    })
                },
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ApplicationNameText()
                Card(
                    modifier = Modifier
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
                                            navController.navigate(AuthScreen.Auth.route) {
                                                popUpTo(AuthScreen.Auth.route) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                )

                                TextHeaderBoxScope(
                                    "Register\nCategory",
                                    Modifier
                                        .align(Alignment.Center)
                                        .padding(horizontal = 20.dp, vertical = 20.dp)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CategoryNavigateButton(
                                buttonName = "Member",
                                imageId = R.drawable.memberregister,
                                screenRoute = AuthScreen.RegisterMemberScreen.route,
                                navController = navController
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 25.dp)
                        ) {
                            CategoryNavigateButton(
                                buttonName = "Trainer",
                                imageId = R.drawable.trainerregister,
                                screenRoute = AuthScreen.RegisterTrainerScreen.route,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}