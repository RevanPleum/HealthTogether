package healthtogether.ui.friend

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.heathtogethermobile.R
import healthtogether.components.AppColor
import healthtogether.components.BottomMenu
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User

@SuppressLint("SuspiciousIndentation")
@Composable
fun OtherProfileView(navController: NavController, viewModel: HomeViewModel, id: String) =
    AppTheme {
        var otherUser by remember {
            mutableStateOf(User())
        }

        val request = QueryUsersRequest(
            filter = Filters.`in`("id", id),
            offset = 0,
            limit = 3,
        )

        var otherImage: Bitmap? by remember {
            mutableStateOf(null)
        }
        val otherPainter = rememberAsyncImagePainter(
            if (otherImage == null) R.drawable.profile else otherImage
        )

        if (otherUser.id == "")
            ChatClient.instance().queryUsers(request).enqueue { result ->
                if (result.isSuccess) {
                    Log.v("TestQuery", "user data : ${result.isSuccess}")
                    otherUser = result.data()[0]
                    val imageBytes = Base64.decode(otherUser.image, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(
                        imageBytes,
                        0,
                        imageBytes.size
                    )
                    otherImage = decodedImage
                } else {
                    Log.v("TestQuery", "error : ${result.error().message}")
                }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            ) {

                Card(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .height(650.dp)
                        .fillMaxWidth(),
                    elevation = 20.dp,
                    border = BorderStroke(0.dp, Color.White),
                    shape = RoundedCornerShape(30.dp),

                    ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        //horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),

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


                                Image(
                                    painter = otherPainter,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .padding(top = 20.dp)
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .border(
                                            BorderStroke(1.dp, Color.White),
                                            CircleShape
                                        )
                                        .align(Alignment.Center),
                                    contentScale = ContentScale.Crop,
                                )
                            }

                        }

                        Text(
                            text = otherUser.name,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W600,
                            color = AppColor.Primary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(450.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Row {
                                Text(
                                    text = "Status : ",
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Left,
                                    fontWeight = FontWeight.W600,
                                    color = AppColor.Primary,
                                    modifier = Modifier
                                        //.fillMaxWidth()
                                        .padding(start = 20.dp)
                                )

                                val status: String =
                                    if (otherUser.extraData.get("status") == "00") "member" else "Trainer"
                                Text(
                                    text = status,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Left,
                                    color = AppColor.Primary,
                                    //modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Box(modifier = Modifier.fillMaxWidth()) {

                                Text(
                                    text = "Age : ",
                                    fontSize = 20.sp,
                                    //textAlign = TextAlign.Left,
                                    fontWeight = FontWeight.W600,
                                    color = AppColor.Primary,
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                )

                                Text(
                                    text = "${otherUser.extraData.get("age")}",
                                    fontSize = 20.sp,
                                    //textAlign = TextAlign.Left,
                                    color = AppColor.Primary,
                                    modifier = Modifier
                                        .padding(start = 80.dp)
                                )

                                Text(
                                    text = "Gender : ",
                                    fontSize = 20.sp,
                                    //textAlign = TextAlign.Justify,
                                    fontWeight = FontWeight.W600,
                                    color = AppColor.Primary,
                                    modifier = Modifier
                                        .padding(end = 110.dp)
                                        .align(Alignment.CenterEnd)
                                )

                                Text(
                                    text = "${otherUser.extraData.get("gender")}",
                                    fontSize = 20.sp,
                                    //textAlign = TextAlign.End,
                                    color = AppColor.Primary,
                                    modifier = Modifier
                                        .padding(end = 40.dp)
                                        .align(Alignment.CenterEnd)
                                )


                            }

                            Text(
                                text = "Favorite Exercise :",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Left,
                                fontWeight = FontWeight.W600,
                                color = AppColor.Primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp)
                            )


                            var checkExerciseEmptyList =
                                otherUser.extraData.get("exercise").toString().removePrefix("[")
                                    .removeSuffix("]").split(",").map { it.trim() }

                            if (checkExerciseEmptyList[0] == "" || checkExerciseEmptyList[0] == "null")
                                checkExerciseEmptyList = emptyList()


                            if (checkExerciseEmptyList.isNullOrEmpty())
                                Text(
                                    text = "None",
                                    modifier = Modifier.padding(start = 40.dp)
                                )
                            else
                                checkExerciseEmptyList.forEach { index ->
                                    Text(
                                        text = "${checkExerciseEmptyList.indexOf(index) + 1}.$index",
                                        modifier = Modifier.padding(start = 40.dp)
                                    )
                                }



                            Text(
                                text = "Favorite Place for Exercise :",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Left,
                                fontWeight = FontWeight.W600,
                                color = AppColor.Primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp)
                            )

                            var checkPlaceEmptyList =
                                otherUser.extraData.get("place").toString().removePrefix("[")
                                    .removeSuffix("]").split(",").map { it.trim() }
                            if (checkPlaceEmptyList[0] == "" || checkPlaceEmptyList[0] == "null")
                                checkPlaceEmptyList = emptyList()

                            if (checkPlaceEmptyList.isNullOrEmpty())
                                Text(
                                    text = "None",
                                    modifier = Modifier.padding(start = 40.dp)
                                )
                            else
                                checkPlaceEmptyList.forEach { index ->
                                    Text(
                                        text = "${checkPlaceEmptyList.indexOf(index) + 1}.$index",
                                        modifier = Modifier.padding(start = 40.dp)
                                    )
                                }

                            Button(
                                onClick = {
//                                navController.navigate(HomeScreen.EditProfileScreen.route)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    AppColor.Primary
                                ),
                                elevation = ButtonDefaults.elevation(
                                    5.dp
                                ),
                                border = BorderStroke(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(20),
                                modifier = Modifier
                                    .padding(start = 70.dp, end = 70.dp)
                                    .fillMaxWidth()
                                    .height(50.dp)

                            ) {
                                Text(
                                    text = "Add Friend",
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.W600,
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = {
                                    /*TODO*/
                                },
                                colors = ButtonDefaults.buttonColors(
                                    Color.Red
                                ),
                                elevation = ButtonDefaults.elevation(
                                    5.dp
                                ),
                                border = BorderStroke(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(20),
                                modifier = Modifier
                                    .padding(start = 70.dp, end = 70.dp)
                                    .fillMaxWidth()
                                    .height(50.dp)

                            ) {
                                Text(
                                    text = "Block",
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.W600,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
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