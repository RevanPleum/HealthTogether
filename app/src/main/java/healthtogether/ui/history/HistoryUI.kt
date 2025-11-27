package healthtogether.ui.history

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.components.AppColor
import healthtogether.components.BottomMenu
import healthtogether.components.ShowLoading
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel
import io.getstream.chat.android.client.models.Channel
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HistoryView(
    navController: NavController,
    viewModel: HomeViewModel,
    channel: ChannelListViewModel
) = AppTheme {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var daySelect by remember {
        mutableIntStateOf(-1)
    }

    var monthSelect by remember {
        mutableIntStateOf(-1)
    }

    var yearSelect by remember {
        mutableIntStateOf(-1)
    }


    val setDateForSearch = DatePickerDialog(
        context
    )

    var selectDate by remember {
        mutableStateOf("All")
    }

    setDateForSearch.setOnDateSetListener { _, year, month, dayOfMonth ->
        yearSelect = year
        monthSelect = month + 1
        daySelect = dayOfMonth
        selectDate = "$dayOfMonth/${month + 1}/$year"
    }

    var dateJoinCheck by remember {
        mutableStateOf("None")
    }

    var listChannel by remember {
        mutableStateOf(listOf<Channel>())
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        isLoading = true
        listChannel = channel.getChannel("none")
        isLoading = false
    }





    Surface {
        Modifier.fillMaxSize()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
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
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomMenu(state = viewModel.state, navController = navController)
                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.size(20.dp))

                    Text(
                        text = "History Exercise",
                        color = AppColor.Primary,
                        fontWeight = FontWeight.W700,
                        fontSize = 20.sp,
                    )


                    Spacer(modifier = Modifier.size(20.dp))



                    Row(
                        modifier = Modifier.fillMaxWidth(.9f),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {


                        Text(
                            text = "Date",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W500,
                            color = AppColor.Primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 0.dp)
                                .align(Alignment.CenterVertically)
                        )

                        val memberNameInteractionSource = remember { MutableInteractionSource() }



                        Button(
                            onClick = {
                                selectDate = "All"
                            },
                            colors = ButtonDefaults.buttonColors(
                                Color.White
                            ),
                            border = BorderStroke(1.dp, Color.LightGray),
                            shape = RoundedCornerShape(20),
                            modifier = Modifier
                                .size(width = 200.dp, height = 40.dp)

                        ) {
                            Text(
                                text = selectDate,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.W600,
                                color = Color.Black
                            )
                        }



                        Image(painterResource(id = R.drawable.iconcalender),
                            contentDescription = "Calender Icon",
                            modifier = Modifier
                                .clickable {
                                    setDateForSearch.show()
                                }
                                .size(40.dp)
                                .align(Alignment.CenterVertically)
                        )


                    }

                    Spacer(modifier = Modifier.size(10.dp))

//                    Button(
//                        onClick = {
//
//
//                        },
//                        colors = ButtonDefaults.buttonColors(AppColor.Primary),
//                        shape = RoundedCornerShape(20.dp),
//                        modifier = Modifier.padding(top = 10.dp)
//                    ) {
//                        Image(
//                            painterResource(id = R.drawable.iconsearch),
//                            contentDescription = "Search Icon",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier.padding(end = 5.dp)
//                        )
//
//                        Text(
//                            text = "Search",
//                            color = Color.White,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.W700,
//                        )
//                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .width(1.dp)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    if (isLoading)
                        ShowLoading()

                    else
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .fillMaxHeight(.9f)
                            .verticalScroll(rememberScrollState())
                    ) {

                        Spacer(modifier = Modifier.size(10.dp))

                        Log.v("History", "History Screen")

                        listChannel.sortedByDescending { it.createdAt }.forEach { item ->


                            val dateJoin =
                                SimpleDateFormat("d/M/yyyy").format(item.createdAt!!).toString()


                            if (dateJoin != dateJoinCheck && selectDate == "All") {
                                Text(
                                    text = dateJoin,
                                    color = AppColor.BorderGrey,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.W700,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.size(10.dp))

                            dateJoinCheck = dateJoin

                            Log.v("History", "selectDate : $selectDate , date : $dateJoin")

                            if (selectDate == "All" || selectDate == SimpleDateFormat("d/M/yyyy").format(
                                    item.createdAt!!
                                ).toString()
                            ) {
                                Button(
                                    onClick = {

                                    },
                                    colors = ButtonDefaults.buttonColors(Color.White),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                                    contentPadding = PaddingValues(0.dp)

                                ) {
                                    Card(
                                        modifier = Modifier
                                            .height(100.dp)
                                            .width(10.dp),
                                        shape = RoundedCornerShape(
                                            topStart = 30.dp,
                                            topEnd = 30.dp,
                                            bottomEnd = 30.dp,
                                            bottomStart = 30.dp
                                        ), backgroundColor = AppColor.Primary
                                    ) {}
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 10.dp, top = 7.dp, bottom = 7.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                            //.padding(top = 5.dp)
                                        )
                                        {
                                            Text(
                                                text = "Name : ",
                                                color = AppColor.Primary,
                                                fontWeight = FontWeight.W700,
                                                fontSize = 18.sp,
                                            )

                                            Text(
                                                text = item.name,
                                                color = AppColor.Primary,
                                                textAlign = TextAlign.Center,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.W400,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                            //.padding(top = 5.dp)
                                        )
                                        {
                                            Text(
                                                text = "Category : ",
                                                color = AppColor.Primary,
                                                fontWeight = FontWeight.W700,
                                                fontSize = 18.sp,
                                            )

                                            Text(
                                                text = "${item.extraData.get("exercise")}",
                                                color = AppColor.Primary,
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.W400,
                                                fontSize = 18.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )

                                            if(item.extraData["placeAddress"] != null)
                                            Text(
                                                text = "${item.memberCount}/${
                                                    item.extraData["maxMember"].toString()
                                                        .toDoubleOrNull()?.roundToInt()
                                                }",
                                                color = AppColor.Primary,
                                                textAlign = TextAlign.End,
                                                fontWeight = FontWeight.W700,
                                                fontSize = 18.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(end = 20.dp)
                                            )

                                            else
                                                Row(
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .padding(end = 30.dp)
                                                ) {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.iconviewer),
                                                        contentDescription = "Viewer Icon",
                                                        modifier = Modifier
                                                            .padding(end = 5.dp)
                                                            .align(Alignment.CenterVertically)
                                                            .size(20.dp),
                                                    )

                                                    Text(
                                                        text = "${item.memberCount}",
                                                        color = AppColor.Primary,
                                                        fontWeight = FontWeight.W700,
                                                        fontSize = 18.sp,
                                                        modifier = Modifier
                                                            .padding(end = 0.dp)
                                                    )
                                                }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                            //.padding(top = 5.dp)
                                        )
                                        {

                                            Text(
                                                text = "Time : ",
                                                color = AppColor.Primary,
                                                fontWeight = FontWeight.W500,
                                                fontSize = 18.sp,
                                            )

                                            val roomTime =
                                                item.extraData.get("time") as? Map<String, Any>
                                            Text(
                                                text = "${roomTime?.get("timeStart")} - ${
                                                    roomTime?.get(
                                                        "timeEnd"
                                                    )
                                                }",
                                                color = AppColor.Primary,
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.W400,
                                                fontSize = 18.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }


                                }
                            }

                            Spacer(modifier = Modifier.size(10.dp))
                        }


                    }


                    Spacer(modifier = Modifier.size(10.dp))
                }


            }

        }


    }


}

