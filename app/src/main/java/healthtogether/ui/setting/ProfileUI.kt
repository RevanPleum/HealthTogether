package healthtogether.ui.setting


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.heathtogethermobile.R
import healthtogether.apiService.model.Dropdowns.BaseDropdownViewModel
import healthtogether.components.AppColor
import healthtogether.components.BottomMenu
import healthtogether.components.HomeScreen
import healthtogether.components.ShowLoading
import healthtogether.components.TopAppNameAndProfile
import healthtogether.components.encodeImage
import healthtogether.components.getExerciseList
import healthtogether.components.getGenderList
import healthtogether.components.getPlace
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel
import java.io.ByteArrayOutputStream


@Composable
fun ProfileView(navController: NavController, viewModel: HomeViewModel) = AppTheme {
    val imageBytes = Base64.decode(viewModel.state.imageProfile, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(
        imageBytes,
        0,
        imageBytes.size
    )
    val painter = rememberAsyncImagePainter(
        if (viewModel.state.imageProfile == "") R.drawable.profile else decodedImage
    )



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
                    .height(700.dp)
                    .padding(top = 20.dp)
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
                                painter = painter,
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
                        text = "${viewModel.state.userName}",
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
                            .height(550.dp)
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

                            Text(
                                text = "Member",
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
                                text = "${viewModel.state.userAge}",
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
                                text = "${viewModel.state.userGender}",
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



                        if (viewModel.state.userExerciseFav.isEmpty())
                            Text(
                                text = "None",
                                modifier = Modifier.padding(start = 40.dp)
                            )
                        else {

                            viewModel.state.userExerciseFav.forEach { index ->
                                Text(
                                    text = "${viewModel.state.userExerciseFav.indexOf(index) + 1}.$index",
                                    modifier = Modifier.padding(start = 40.dp)
                                )
                            }
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

                        if (viewModel.state.userPlaceFav.isEmpty())
                            Text(
                                text = "None",
                                modifier = Modifier.padding(start = 40.dp)
                            )
                        else
                            viewModel.state.userPlaceFav.forEach { index ->
                                Text(
                                    text = "${viewModel.state.userPlaceFav.indexOf(index) + 1}.$index",
                                    modifier = Modifier.padding(start = 40.dp)
                                )
                            }

                        Button(
                            onClick = { navController.navigate(HomeScreen.EditProfileScreen.route) },
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
                                text = "Edit Profile",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.W600,
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = { navController.navigate(HomeScreen.UpgradeToTrainerScreen.route) },
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
                                text = "Upgrade to Trainer",
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


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun EditProfileView(navController: NavController, viewModel: HomeViewModel) = AppTheme {
    var exerciseFav by remember {
        mutableStateOf(viewModel.state.userExerciseFav.toMutableStateList())
    }
    var placeFav by remember {
        mutableStateOf(viewModel.state.userPlaceFav.toMutableStateList())
    }
    var showAgeItem by remember { mutableStateOf(false) }
    var showGenderItem by remember { mutableStateOf(false) }
    var name by remember {
        mutableStateOf(viewModel.state.userName)
    }
    var exerciseFavList by remember {
        mutableStateOf(
            listOf("")
        )
    }

    var placeFavList by remember {
        mutableStateOf(
            listOf("")
        )
    }

    var genderList by remember {
        mutableStateOf(
            listOf(BaseDropdownViewModel())
        )
    }

    LaunchedEffect(Unit) {
        exerciseFavList = getExerciseList("EN").map { it.text }
        placeFavList = getPlace("1","6","34","EN").map { it.text }
        genderList = getGenderList("EN")
    }


    var age by remember {
        mutableStateOf(viewModel.state.userAge)
    }

    var gender by remember {
        mutableStateOf(viewModel.state.userGender)
    }

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    var imageBytes = Base64.decode(viewModel.state.imageProfile, Base64.DEFAULT)
    var decodedImage = BitmapFactory.decodeByteArray(
        imageBytes,
        0,
        imageBytes.size
    )


    var imageProfile by rememberSaveable { mutableStateOf("") }
    var painter = rememberAsyncImagePainter(
        if (decodedImage == null && imageProfile == "")
            R.drawable.profile
        else if (imageProfile == "")
            decodedImage
        else
            imageProfile
    )


    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                imageProfile = it.toString()
            }
        }
    )

    var encodedImage by rememberSaveable { mutableStateOf("") }

    var isLoading by remember {
        mutableStateOf(false)
    }

    if (isLoading)
        LaunchedEffect (Unit){
            viewModel.updateProfile(
                if(imageProfile == "") viewModel.state.imageProfile else encodedImage,
                name,
                age,
                gender,
                exerciseFav,
                placeFav
            )
                Log.v("ImageProfile",encodedImage)
            isLoading = false

           navController.popBackStack()
        }



    Surface {
        Modifier.fillMaxSize()
    }

    if(isLoading)
        ShowLoading()

    else
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            TopAppNameAndProfile(
                navController = navController,
                viewModel.state.imageProfile.toString()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {


            Card(
                modifier = Modifier
                    .height(700.dp)
                    .padding(top = 20.dp)
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
                                painter = painter,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .border(
                                        BorderStroke(1.dp, Color.LightGray),
                                        CircleShape
                                    )
                                    .align(Alignment.Center)
                                    .clickable {
                                        imagePicker.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )

                                    },
                                contentScale = ContentScale.Crop,
                            )
                        }

                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(width = 150.dp, height = 50.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(550.dp)
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

                            Text(
                                text = "Member",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Left,
                                color = AppColor.Primary,
                                //modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()

                        ) {

                            Text(
                                text = "Age : ",
                                fontSize = 20.sp,
                                //textAlign = TextAlign.Left,
                                fontWeight = FontWeight.W600,
                                color = AppColor.Primary,
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .align(Alignment.CenterStart)

                            )
                            ExposedDropdownMenuBox(
                                expanded = showAgeItem,
                                onExpandedChange = { showAgeItem = !showAgeItem },
                                modifier = Modifier
                                    .padding(start = 70.dp)
                                    .size(width = 50.dp, height = 50.dp),
                            ) {
                                TextField(
                                    value = age.toString(),
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = LocalTextStyle.current.copy(fontSize = 15.sp)
                                )
                                ExposedDropdownMenu(
                                    expanded = showAgeItem,
                                    onDismissRequest = { showAgeItem = false }
                                ) {
                                    for (index in 18..119) {
                                        DropdownMenuItem(onClick = {
                                            age = index
                                            showAgeItem = false
                                        }
                                        ) {
                                            Text(text = "$index", fontSize = 15.sp)
                                        }
                                    }
                                }

                            }



                            Text(
                                text = "Gender : ",
                                fontSize = 20.sp,
                                //textAlign = TextAlign.Justify,
                                fontWeight = FontWeight.W600,
                                color = AppColor.Primary,
                                modifier = Modifier
                                    .padding(end = 120.dp)
                                    .align(Alignment.CenterEnd)
                            )

                            ExposedDropdownMenuBox(
                                expanded = showGenderItem,
                                onExpandedChange = { showGenderItem = !showGenderItem },
                                modifier = Modifier
                                    .padding(end = 20.dp)
                                    .size(width = 100.dp, height = 50.dp)
                                    .align(Alignment.CenterEnd),
                            ) {
                                TextField(
                                    value = gender.toString(),
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = LocalTextStyle.current.copy(fontSize = 15.sp)
                                )
                                ExposedDropdownMenu(
                                    expanded = showGenderItem,
                                    onDismissRequest = { showGenderItem = false }
                                ) {
                                    genderList.forEach { it ->
                                        DropdownMenuItem(onClick = {
                                            gender = it.text
                                            showGenderItem = false
                                        }
                                        ) {
                                            Text(text = it.text, fontSize = 15.sp)
                                        }
                                    }

                                }

                            }

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

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.height((((exerciseFavList.count() / 2) + (exerciseFavList.count() % 2)) * 60).dp)

                        ) {
                            items(exerciseFavList) { index ->
                                Button(
                                    onClick = {
                                        if (exerciseFav.contains(index)) {
                                            exerciseFav.remove(index)
                                        } else {
                                            exerciseFav.add(index)

                                        }
                                    },
                                    colors =
                                    if (exerciseFav.contains(index)) ButtonDefaults.buttonColors(
                                        Color.LightGray
                                    )
                                    else ButtonDefaults.buttonColors(
                                        Color.White
                                    ),
                                    elevation = ButtonDefaults.elevation(
                                        5.dp
                                    ),
                                    border = BorderStroke(1.dp, Color.LightGray),
                                    shape = RoundedCornerShape(20),
                                    modifier = Modifier
                                        .size(width = (index.count() * 20).dp, height = 50.dp)

                                ) {
                                    Text(
                                        text = "$index",
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.W600,
                                        color = Color.Black
                                    )
                                }
                            }

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

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.height((((placeFavList.count() / 2) + (placeFavList.count() % 2)) * 60).dp)

                        ) {
                            items(placeFavList) { index ->
                                Button(
                                    onClick = {
                                        if (placeFav.contains(index)) {
                                            placeFav.remove(index)

                                        } else {
                                            placeFav.add(index)
                                        }
                                    },
                                    colors =
                                    if (placeFav.contains(index)) ButtonDefaults.buttonColors(
                                        Color.LightGray
                                    )
                                    else ButtonDefaults.buttonColors(
                                        Color.White
                                    ),
                                    elevation = ButtonDefaults.elevation(
                                        5.dp
                                    ),
                                    border = BorderStroke(1.dp, Color.LightGray),
                                    shape = RoundedCornerShape(20),
                                    modifier = Modifier
                                        .size(width = (index.count() * 20).dp, height = 50.dp)

                                ) {
                                    Text(
                                        text = "$index",
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.W600,
                                        color = Color.Black
                                    )
                                }
                            }

                        }

                        Button(
                            onClick = {
                                if (imageProfile != "") {
                                    encodedImage = encodeImage(context,imageProfile.toUri(),50)

                                }

                                    isLoading = true



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
                                text = "Confirm",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.W600,
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = {


                                navController.popBackStack()
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
                                text = "Cancel",
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



