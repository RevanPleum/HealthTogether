package healthtogether.ui.liveathome

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.components.AppColor
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RatingLiveView(
    navController: NavController,
    viewModel: HomeViewModel,
    messageViewModel: MessageListViewModel
) = AppTheme {

    val focusManager = LocalFocusManager.current

    var rating by remember {
        mutableIntStateOf(5)
    }

    val starRate = listOf(1,2,3,4,5)


    var ratingInfomation by remember {
        mutableStateOf("")
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


        Card(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize(),
            elevation = 20.dp,
            border = BorderStroke(0.dp, Color.White),
            shape = RoundedCornerShape(30.dp),

            ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.size(10.dp))

                    Image(
                        painterResource(id = R.drawable.iconback),
                        contentDescription = "Back Icon",
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack()
                            }
                            .align(alignment = Alignment.CenterStart)
                            .padding(top = 20.dp, start = 10.dp))

                    Text(
                        text = "Rating From",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W800,
                        color = AppColor.Primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )

                }

                Spacer(modifier = Modifier.size(10.dp))

                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .width(1.dp)
                        .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                )


                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = "Live Name : ${messageViewModel.channel.name}",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.W600,
                    color = AppColor.Primary,
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .align(Alignment.Start)
                )

                Spacer(modifier = Modifier.size(10.dp))

                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Trainer Name : ${messageViewModel.channel.createdBy.name}",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.W600,
                        color = AppColor.Primary,
                        modifier = Modifier
                            .padding(start = 30.dp)


                    )

                }


                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = "Rating",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.W600,
                    color = AppColor.Primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.size(15.dp))

                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(.8f)) {

                    starRate.forEach{ item ->
                        if (item <= rating)
                            Image(painter = painterResource(id = R.drawable.iconstar),
                                contentDescription = "Star Icon",
                                modifier = Modifier.clickable { rating = item })

                        else
                            IconButton(onClick = { rating = item }) {
                                Icon(painter = painterResource(id = R.drawable.iconstar),
                                    contentDescription = "Star Icon",
                                    tint = Color.LightGray
                                    )
                            }

                    }

                }

                Spacer(modifier = Modifier.size(15.dp))

                Text(text = "$rating / 5",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.W600,
                    color = AppColor.Primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally))

                Spacer(modifier = Modifier.size(15.dp))

                OutlinedTextField(
                    value = ratingInfomation,
                    onValueChange = { ratingInfomation = it },
                    placeholder = {
                        Text(
                            text = "More Information...",
                            textAlign = TextAlign.Start,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Start
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),

                    modifier = Modifier
                        .size(width = 350.dp, height = 300.dp)
                )

                Spacer(modifier = Modifier.size(20.dp))


                Button(
                    onClick = {


                    },
                    colors = ButtonDefaults.buttonColors(AppColor.Primary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 15.dp)
                ) {
                    Text(
                        text = "Send Rating",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W700,
                    )
                }


            }
        }
    }
}