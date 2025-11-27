package healthtogether.ui.setting

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.components.AppColor
import healthtogether.components.BottomMenu
import healthtogether.components.SettingScreen
import healthtogether.components.TextHeaderBoxScope
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.MainActivity
import healthtogether.ui.home.HomeActivity
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.home.homemodel.HomeViewState

@Composable
fun SettingView(navController: NavController, state: HomeViewState,viewModel: HomeViewModel) = AppTheme {

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
            TopAppNameAndProfile(navController, state.imageProfile.toString())
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {

            Card(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
                    .height(650.dp),
                elevation = 20.dp,
                border = BorderStroke(0.dp, Color.White),
                shape = RoundedCornerShape(30.dp),

                ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    TextHeaderBoxScope(
                        value = "Setting", modifier = Modifier
                            .padding(top = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            5.dp
                        ),
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .padding(start = 60.dp, end = 60.dp)
                            .fillMaxWidth()
                            .height(50.dp)

                    ) {
                        Text(
                            text = "Change Password",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W600,
                            color = AppColor.Primary
                        )
                    }

                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            5.dp
                        ),
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .padding(start = 60.dp, end = 60.dp)
                            .fillMaxWidth()
                            .height(50.dp)

                    ) {
                        Text(
                            text = "Block List",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W600,
                            color = AppColor.Primary
                        )
                    }

                    Button(
                        onClick = { navController.navigate(SettingScreen.AboutAppScreen.route) },
                        colors = ButtonDefaults.buttonColors(
                            Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            5.dp
                        ),
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .padding(start = 60.dp, end = 60.dp)
                            .fillMaxWidth()
                            .height(50.dp)

                    ) {
                        Text(
                            text = "About Health Together",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W600,
                            color = AppColor.Primary
                        )
                    }

                    Button(
                        onClick = {
                            val intent = Intent(context, MainActivity::class.java)
                            viewModel.Logout()
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            5.dp
                        ),
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .padding(start = 60.dp, end = 60.dp)
                            .fillMaxWidth()
                            .height(50.dp)

                    ) {
                        Text(
                            text = "Logout",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W600,
                            color = AppColor.Primary
                        )
                    }

                    Spacer(modifier = Modifier.height(70.dp))

                }
            }

        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom

        ) {
            BottomMenu(state, navController)
        }
    }
}

@Composable
fun AboutAppView(navController: NavController, state: HomeViewState) = AppTheme {

    val appTerm: String = "It is also possible to have text with both direction scrolling.\n" +
            "\n" +
            "Here the Text is rendered horizontally till the text is completely rendered or an end-of-line character is reached. For each \\n character, a line break happens and rendering continues from the next line.\n" +
            "\n" +
            "If a text has a lot of lines split using \\n , now it is possible to view it completely as vertical scrolling is enabled now." + "\n" + "\n" + "It is also possible to have text with both direction scrolling.\n" +
            "\n" +
            "Here the Text is rendered horizontally till the text is completely rendered or an end-of-line character is reached. For each \\n character, a line break happens and rendering continues from the next line.\n" +
            "\n" +
            "If a text has a lot of lines split using \\n , now it is possible to view it completely as vertical scrolling is enabled now."

    val appVersion: String = "1.0"

    Surface {
        Modifier.fillMaxSize()
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            TopAppNameAndProfile(navController, state.imageProfile.toString())
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 50.dp),
        ) {

            Card(
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 30.dp)
                    .fillMaxWidth(),
                elevation = 20.dp,
                border = BorderStroke(0.dp, Color.White),
                shape = RoundedCornerShape(30.dp),

                ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),

                    ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(painterResource(id = R.drawable.iconback),
                            contentDescription = "Back Icon",
                            modifier = Modifier
                                .clickable {
                                    navController.popBackStack()
                                }
                                .align(alignment = Alignment.CenterStart)
                                .padding(top = 20.dp, start = 10.dp))

                        TextHeaderBoxScope(
                            value = "About Health Together", modifier = Modifier
                                .align(alignment = Alignment.Center)
                                .padding(top = 20.dp)
                        )
                    }



                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = "Current Version    $appVersion",
                        color = AppColor.Primary,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(start = 25.dp)


                    )

                    Text(
                        text = appTerm,
                        modifier = Modifier
                            .height(650.dp)
                            .padding(start = 25.dp, end = 25.dp)
                            .verticalScroll(rememberScrollState())
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                }


            }
        }

    }


    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom

    ) {
        BottomMenu(state, navController)
    }
}



