package healthtogether.ui.outdoorexercise

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.apiService.model.Dropdowns.BaseDropdownViewModel
import healthtogether.components.AppColor
import healthtogether.components.BottomMenu
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.TopAppNameAndProfile
import healthtogether.components.getDistrictList
import healthtogether.components.getExerciseList
import healthtogether.components.getGenderList
import healthtogether.components.getPlace
import healthtogether.components.getProvinceList
import healthtogether.components.getSubDistrictList
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectPlaceView(
    navController: NavController,
    viewModel: HomeViewModel,
    channel: ChannelListViewModel
) =
    AppTheme {

        var showProvinceItem by remember {
            mutableStateOf(false)
        }
        var showDistrictItem by remember {
            mutableStateOf(false)
        }
        var showSubDistrictItem by remember {
            mutableStateOf(false)
        }
        var showPlaceItem by remember {
            mutableStateOf(false)
        }

        var provinceSelect by remember{
            mutableStateOf((BaseDropdownViewModel()))
        }
        var districtSelect by remember {
            mutableStateOf((BaseDropdownViewModel()))
        }
        var subDistrictSelect by remember {
            mutableStateOf((BaseDropdownViewModel()))
        }
        var placeSelect by remember {
            mutableStateOf((BaseDropdownViewModel()))
        }
        var roomName by remember {
            mutableStateOf("")
        }
        var showExerciseCategory by remember {
            mutableStateOf(false)
        }

        var exerciseSelect by remember {
            mutableStateOf("")
        }


        var exerciseCategoryItem by remember {
            mutableStateOf(
                listOf(BaseDropdownViewModel())
            )
        }

        var showGenderCategory by remember {
            mutableStateOf(false)
        }

        var genderSelect by remember {
            mutableStateOf("")
        }

        var genderCategoryItem by remember {
            mutableStateOf(
                listOf(BaseDropdownViewModel())
            )
        }

        var provinceList by remember {
            mutableStateOf(listOf(BaseDropdownViewModel()))
        }

        var provinceID by remember {
            mutableStateOf("")
        }


        var districtList by remember {
            mutableStateOf(listOf(BaseDropdownViewModel()))
        }

        var districtID by remember {
            mutableStateOf("")
        }


        var subDistrictList by remember {
            mutableStateOf(listOf(BaseDropdownViewModel()))
        }

        var subDistrictID by remember {
            mutableStateOf("")
        }

        var placeList by remember {
            mutableStateOf(listOf(BaseDropdownViewModel()))
        }

        var placeID by remember {
            mutableStateOf("")
        }

        LaunchedEffect(Unit) {
             provinceList = getProvinceList("EN")

        }

        LaunchedEffect(provinceID) {
            if(provinceID != "")
                districtList = getDistrictList(provinceID,"EN")
        }

        LaunchedEffect(districtID) {
            if(districtID != "")
                subDistrictList = getSubDistrictList(districtID,"EN")
        }

        LaunchedEffect(subDistrictID) {
            if(subDistrictID != "")
               placeList = getPlace(provinceID,districtID,subDistrictID,"EN")
        }


        LaunchedEffect(Unit) {
            exerciseCategoryItem = getExerciseList("EN")

        }

        LaunchedEffect(Unit) {
            genderCategoryItem = getGenderList("EN")

        }


        val provinceInteractionSource = remember { MutableInteractionSource() }
        val districtInteractionSource = remember { MutableInteractionSource() }
        val subDistrictInteractionSource = remember { MutableInteractionSource() }
        val placeInteractionSource = remember { MutableInteractionSource() }
        val roomNameInteractionSource = remember { MutableInteractionSource() }
        val exerciseInteractionSource = remember { MutableInteractionSource() }
        val genderInteractionSource = remember { MutableInteractionSource() }
        val focusManager = LocalFocusManager.current



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
                        .fillMaxWidth()
                        .height(650.dp),
                    elevation = 20.dp,
                    border = BorderStroke(0.dp, Color.White),
                    shape = RoundedCornerShape(30.dp),

                    ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

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


                            Text(
                                text = "Outdoor Excercise",
                                fontSize = 25.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.W600,
                                color = AppColor.Primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                            )
                        }

                        Text(
                            text = "Select Place",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W400,
                            color = AppColor.Primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, bottom = 20.dp)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(550.dp)
                                .verticalScroll(rememberScrollState())
                        ) {


                            ExposedDropdownMenuBox(
                                expanded = showProvinceItem,
                                onExpandedChange = { showProvinceItem = !showProvinceItem },
                                modifier = Modifier.padding(bottom = 10.dp)

                            ) {
                                BasicTextField(
                                    value = provinceSelect.text,
                                    onValueChange = {},
                                    readOnly = true,
                                    interactionSource = provinceInteractionSource,
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    ),

                                    modifier = Modifier.size(width = 270.dp, height = 50.dp),
                                ) {
                                    TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                        value = provinceSelect.text,
                                        innerTextField = it,
                                        enabled = true,
                                        singleLine = true,
                                        visualTransformation = VisualTransformation.None,
                                        interactionSource = provinceInteractionSource,
                                        contentPadding = PaddingValues(start = 15.dp, end = 30.dp),
                                        placeholder = {
                                            Text(
                                                text = "Select Province",
                                                textAlign = TextAlign.Start,
                                                color = Color.Gray,
                                            )
                                        },
                                        trailingIcon =
                                        {
                                            IconButton(
                                                onClick = { }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.dropdown_icon),
                                                    contentDescription = "Dropdown Icon",
                                                    tint = AppColor.Primary
                                                )
                                            }
                                        },
                                        border = {
                                            TextFieldDefaults.BorderBox(
                                                enabled = true,
                                                isError = false,
                                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                                    unfocusedBorderColor = Color.Gray,
                                                    focusedBorderColor = AppColor.Primary
                                                ),
                                                interactionSource = provinceInteractionSource,
                                                shape = RoundedCornerShape(15.dp),
                                                unfocusedBorderThickness = 1.dp,
                                                focusedBorderThickness = 2.dp
                                            )
                                        }
                                    )
                                }


                                ExposedDropdownMenu(
                                    expanded = showProvinceItem,
                                    onDismissRequest = { showProvinceItem = false }
                                ) {
                                    provinceList.forEach { item ->

                                        DropdownMenuItem(onClick = {
                                            provinceSelect = item
                                            provinceID = item.value
                                            districtSelect = BaseDropdownViewModel()
                                            subDistrictSelect = BaseDropdownViewModel()
                                            placeSelect = BaseDropdownViewModel()
                                            showProvinceItem = false


                                        }
                                        ) {
                                            Text(text = item.text, fontSize = 15.sp)
                                        }
                                    }
                                }
                            }




                            ExposedDropdownMenuBox(
                                expanded = if (provinceSelect != BaseDropdownViewModel()) showDistrictItem else false,
                                onExpandedChange = {
                                    if (provinceSelect != BaseDropdownViewModel()) showDistrictItem = !showDistrictItem
                                },
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                            ) {
                                BasicTextField(
                                    value = districtSelect.text,
                                    enabled = provinceSelect != BaseDropdownViewModel(),
                                    onValueChange = {},
                                    readOnly = true,
                                    interactionSource = districtInteractionSource,
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    ),
                                    modifier = Modifier.size(width = 270.dp, height = 50.dp),
                                ) {
                                    TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                        value = districtSelect.text,
                                        innerTextField = it,
                                        enabled = true,
                                        singleLine = true,
                                        visualTransformation = VisualTransformation.None,
                                        interactionSource = districtInteractionSource,
                                        contentPadding = PaddingValues(
                                            start = 15.dp,
                                            end = 30.dp
                                        ),
                                        placeholder = {
                                            Text(
                                                text = "Select District",
                                                textAlign = TextAlign.Start,
                                                color = Color.Gray,
                                            )
                                        },
                                        trailingIcon =
                                        {
                                            IconButton(
                                                enabled = provinceSelect != BaseDropdownViewModel(),
                                                onClick = { }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.dropdown_icon),
                                                    contentDescription = "Dropdown Icon",
                                                    tint = if (provinceSelect != BaseDropdownViewModel()) AppColor.Primary else AppColor.BorderGrey
                                                )
                                            }
                                        },
                                        border = {
                                            TextFieldDefaults.BorderBox(
                                                enabled = true,
                                                isError = false,
                                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                                    unfocusedBorderColor = Color.Gray,
                                                    focusedBorderColor = if (provinceSelect != BaseDropdownViewModel()) AppColor.Primary else Color.Gray
                                                ),
                                                interactionSource = districtInteractionSource,
                                                shape = RoundedCornerShape(15.dp),
                                                unfocusedBorderThickness = 1.dp,
                                                focusedBorderThickness = if (provinceSelect != BaseDropdownViewModel()) 2.dp else 1.dp
                                            )
                                        }
                                    )
                                }


                                ExposedDropdownMenu(
                                    expanded = showDistrictItem,
                                    onDismissRequest = { showDistrictItem = false }
                                ) {
                                    districtList.forEach { item ->

                                        DropdownMenuItem(onClick = {
                                            districtSelect = item
                                            districtID = item.value
                                            subDistrictSelect = BaseDropdownViewModel()
                                            placeSelect = BaseDropdownViewModel()
                                            showDistrictItem = false
                                        }
                                        ) {
                                            Text(text = item.text, fontSize = 15.sp)
                                        }
                                    }
                                }
                            }



                            ExposedDropdownMenuBox(
                                expanded = showSubDistrictItem,
                                onExpandedChange = {
                                    if (districtSelect != BaseDropdownViewModel()) showSubDistrictItem =
                                        !showSubDistrictItem
                                },
                                modifier = Modifier
                                    .padding(bottom = 10.dp),
                            ) {
                                BasicTextField(
                                    value = subDistrictSelect.text,
                                    enabled = districtSelect != BaseDropdownViewModel(),
                                    onValueChange = {},
                                    readOnly = true,
                                    interactionSource = subDistrictInteractionSource,
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    ),
                                    modifier = Modifier.size(width = 270.dp, height = 50.dp),
                                ) {
                                    TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                        value = subDistrictSelect.text,
                                        innerTextField = it,
                                        enabled = true,
                                        singleLine = true,
                                        visualTransformation = VisualTransformation.None,
                                        interactionSource = subDistrictInteractionSource,
                                        contentPadding = PaddingValues(
                                            start = 15.dp,
                                            end = 30.dp
                                        ),
                                        placeholder = {
                                            Text(
                                                text = "Select Subdistrict",
                                                textAlign = TextAlign.Center,
                                                color = Color.Gray,
                                            )

                                        },
                                        trailingIcon =
                                        {
                                            IconButton(
                                                enabled = districtSelect != BaseDropdownViewModel(),
                                                onClick = { }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.dropdown_icon),
                                                    contentDescription = "Dropdown Icon",
                                                    tint = if (districtSelect != BaseDropdownViewModel()) AppColor.Primary else AppColor.BorderGrey
                                                )
                                            }
                                        },
                                        border = {
                                            TextFieldDefaults.BorderBox(
                                                enabled = true,
                                                isError = false,
                                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                                    unfocusedBorderColor = Color.Gray,
                                                    focusedBorderColor = if (districtSelect != BaseDropdownViewModel()) AppColor.Primary else Color.Gray
                                                ),
                                                interactionSource = subDistrictInteractionSource,
                                                shape = RoundedCornerShape(15.dp),
                                                unfocusedBorderThickness = 1.dp,
                                                focusedBorderThickness = if (districtSelect != BaseDropdownViewModel()) 2.dp else 1.dp
                                            )
                                        }
                                    )
                                }
                                ExposedDropdownMenu(
                                    expanded = showSubDistrictItem,
                                    onDismissRequest = { showSubDistrictItem = false }
                                ) {
                                    subDistrictList.forEach { item ->

                                        DropdownMenuItem(onClick = {
                                            subDistrictSelect = item
                                            subDistrictID = item.value
                                            placeSelect = BaseDropdownViewModel()
                                            showSubDistrictItem = false
                                        }
                                        ) {
                                            Text(text = item.text, fontSize = 15.sp)
                                        }
                                    }
                                }
                            }




                            ExposedDropdownMenuBox(
                                expanded = showPlaceItem,
                                onExpandedChange = {
                                    if (subDistrictSelect != BaseDropdownViewModel()) showPlaceItem = !showPlaceItem
                                },
                                modifier = Modifier
                                    .padding(bottom = 10.dp),
                            ) {
                                BasicTextField(
                                    value = placeSelect.text,
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = subDistrictSelect != BaseDropdownViewModel(),
                                    interactionSource = placeInteractionSource,
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    ),
                                    modifier = Modifier.size(width = 270.dp, height = 50.dp),
                                ) {
                                    TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                        value = placeSelect.text,
                                        innerTextField = it,
                                        enabled = true,
                                        singleLine = true,
                                        visualTransformation = VisualTransformation.None,
                                        interactionSource = placeInteractionSource,
                                        contentPadding = PaddingValues(
                                            start = 15.dp,
                                            end = 30.dp
                                        ),
                                        placeholder = {
                                            Text(
                                                text = "Select Place",
                                                textAlign = TextAlign.Start,
                                                color = Color.Gray,
                                            )
                                        },
                                        trailingIcon =
                                        {
                                            IconButton(
                                                enabled = subDistrictSelect != BaseDropdownViewModel(),
                                                onClick = { }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.dropdown_icon),
                                                    contentDescription = "Dropdown Icon",
                                                    tint = if (subDistrictSelect == BaseDropdownViewModel()) AppColor.BorderGrey else AppColor.Primary
                                                )
                                            }
                                        },
                                        border = {
                                            TextFieldDefaults.BorderBox(
                                                enabled = true,
                                                isError = false,
                                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                                    unfocusedBorderColor = Color.Gray,
                                                    focusedBorderColor = if (subDistrictSelect == BaseDropdownViewModel()) Color.Gray else AppColor.Primary
                                                ),
                                                interactionSource = placeInteractionSource,
                                                shape = RoundedCornerShape(15.dp),
                                                unfocusedBorderThickness = 1.dp,
                                                focusedBorderThickness = if (subDistrictSelect == BaseDropdownViewModel()) 1.dp else 2.dp
                                            )
                                        }
                                    )
                                }

                                ExposedDropdownMenu(
                                    expanded = showPlaceItem,
                                    onDismissRequest = { showPlaceItem = false }
                                ) {
                                    placeList.forEach { item ->

                                        DropdownMenuItem(onClick = {
                                            placeSelect = item
                                            placeID = item.value
                                            showPlaceItem = false
                                        }
                                        ) {
                                            Text(text = item.text, fontSize = 15.sp)
                                        }
                                    }
                                }
                            }


                            Text(
                                text = "Optional",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.W400,
                                color = AppColor.Primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp, bottom = 20.dp)
                            )


                            BasicTextField(
                                value = roomName,
                                onValueChange = { roomName = it },
                                readOnly = false,
                                interactionSource = roomNameInteractionSource,
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 15.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                ),
                                modifier = Modifier
                                    .size(width = 270.dp, height = 60.dp)
                                    .padding(bottom = 10.dp),
                            ) {
                                TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                    value = roomName,
                                    innerTextField = it,
                                    enabled = true,
                                    singleLine = true,
                                    visualTransformation = VisualTransformation.None,
                                    interactionSource = districtInteractionSource,
                                    contentPadding = PaddingValues(
                                        start = 15.dp,
                                        end = 30.dp
                                    ),
                                    placeholder = {
                                        Text(
                                            text = "Room Name",
                                            textAlign = TextAlign.Center,
                                            color = Color.Gray,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    border = {
                                        TextFieldDefaults.BorderBox(
                                            enabled = true,
                                            isError = false,
                                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                                unfocusedBorderColor = Color.Gray,
                                                focusedBorderColor = AppColor.Primary
                                            ),
                                            interactionSource = roomNameInteractionSource,
                                            shape = RoundedCornerShape(15.dp),
                                            unfocusedBorderThickness = 1.dp,
                                            focusedBorderThickness = 2.dp
                                        )
                                    }
                                )
                            }

                            ExposedDropdownMenuBox(
                                expanded = showExerciseCategory,
                                onExpandedChange = { showExerciseCategory = !showExerciseCategory },
                                modifier = Modifier
                                    .padding(bottom = 10.dp),
                            ) {
                                BasicTextField(
                                    value = exerciseSelect,
                                    onValueChange = {},
                                    readOnly = true,
                                    interactionSource = exerciseInteractionSource,
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    ),
                                    modifier = Modifier.size(width = 270.dp, height = 50.dp),
                                ) {
                                    TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                        value = exerciseSelect,
                                        innerTextField = it,
                                        enabled = true,
                                        singleLine = true,
                                        visualTransformation = VisualTransformation.None,
                                        interactionSource = exerciseInteractionSource,
                                        contentPadding = PaddingValues(
                                            start = 15.dp,
                                            end = 30.dp
                                        ),
                                        placeholder = {
                                            Text(
                                                text = "Select Exercise",
                                                textAlign = TextAlign.Start,
                                                color = Color.Gray,
                                            )
                                        },
                                        trailingIcon =
                                        {
                                            IconButton(
                                                onClick = { }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.dropdown_icon),
                                                    contentDescription = "Dropdown Icon",
                                                    tint = AppColor.Primary
                                                )
                                            }
                                        },
                                        border = {
                                            TextFieldDefaults.BorderBox(
                                                enabled = true,
                                                isError = false,
                                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                                    unfocusedBorderColor = Color.Gray,
                                                    focusedBorderColor = AppColor.Primary
                                                ),
                                                interactionSource = exerciseInteractionSource,
                                                shape = RoundedCornerShape(15.dp),
                                                unfocusedBorderThickness = 1.dp,
                                                focusedBorderThickness = 2.dp
                                            )
                                        }
                                    )
                                }

                                ExposedDropdownMenu(
                                    expanded = showExerciseCategory,
                                    onDismissRequest = { showExerciseCategory = false }
                                ) {
                                    exerciseCategoryItem.forEach { item ->

                                        DropdownMenuItem(onClick = {
                                            exerciseSelect = item.text
                                            showExerciseCategory = false
                                        }
                                        ) {
                                            Text(text = item.text, fontSize = 15.sp)
                                        }
                                    }
                                }
                            }

                            ExposedDropdownMenuBox(
                                expanded = showGenderCategory,
                                onExpandedChange = { showGenderCategory = !showGenderCategory },
                                modifier = Modifier
                                    .padding(bottom = 10.dp),
                            ) {
                                BasicTextField(
                                    value = genderSelect,
                                    onValueChange = {},
                                    readOnly = true,
                                    interactionSource = genderInteractionSource,
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    ),
                                    modifier = Modifier.size(width = 270.dp, height = 50.dp),
                                ) {
                                    TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                        value = genderSelect,
                                        innerTextField = it,
                                        enabled = true,
                                        singleLine = true,
                                        visualTransformation = VisualTransformation.None,
                                        interactionSource = genderInteractionSource,
                                        contentPadding = PaddingValues(
                                            start = 15.dp,
                                            end = 30.dp
                                        ),
                                        placeholder = {
                                            Text(
                                                text = "Select Gender",
                                                textAlign = TextAlign.Start,
                                                color = Color.Gray,
                                            )
                                        },
                                        trailingIcon =
                                        {
                                            IconButton(
                                                onClick = { }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.dropdown_icon),
                                                    contentDescription = "Dropdown Icon",
                                                    tint = AppColor.Primary
                                                )
                                            }
                                        },
                                        border = {
                                            TextFieldDefaults.BorderBox(
                                                enabled = true,
                                                isError = false,
                                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                                    unfocusedBorderColor = Color.Gray,
                                                    focusedBorderColor = AppColor.Primary
                                                ),
                                                interactionSource = genderInteractionSource,
                                                shape = RoundedCornerShape(15.dp),
                                                unfocusedBorderThickness = 1.dp,
                                                focusedBorderThickness = 2.dp
                                            )
                                        }
                                    )
                                }

                                ExposedDropdownMenu(
                                    expanded = showGenderCategory,
                                    onDismissRequest = { showGenderCategory = false }
                                ) {
                                    genderCategoryItem.forEach { item ->

                                        DropdownMenuItem(onClick = {
                                            genderSelect = item.text
                                            showGenderCategory = false
                                        }
                                        ) {
                                            Text(text = item.text, fontSize = 15.sp)
                                        }
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    //channel.createChannel("TestCreateRoomOnApp", maxMember = 12, category = "Running")
                                    navController.navigate(
                                        OutdoorExerciseScreen.SelectGroup.routeWithSelectPlace(
                                            provinceSelect.value,
                                            districtSelect.value,
                                            subDistrictSelect.value,
                                            placeSelect.value,
                                            placeSelect.text,
                                            if (roomName.isNullOrEmpty()) "none" else roomName,
                                            if (exerciseSelect.isNullOrEmpty()) "none" else exerciseSelect,
                                            if (genderSelect.isNullOrEmpty()) "none" else genderSelect
                                        )
                                    )
                                },
                                enabled = (placeSelect.text != ""),
                                colors = ButtonDefaults.buttonColors(AppColor.Primary),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.padding(top = 10.dp)
                            ) {
                                Text(
                                    text = "Search",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W700,
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

