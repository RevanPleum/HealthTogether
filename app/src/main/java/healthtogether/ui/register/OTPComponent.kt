package healthtogether.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import healthtogether.components.AppColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable

fun OTPBox(
    otpList: List<MutableState<String>>,
    requesterList: List<FocusRequester>
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    Surface(
//        modifier = Modifier
//            .fillMaxSize(),
        color = Color.White
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp)
                    .align(Alignment.TopCenter)
            ) {
                for (i in otpList.indices) {
                    InputOPTView(
                        value = otpList[i].value,
                        onValueChange = { newValue ->
                            //if old value is not empty just return
                            if (otpList[i].value.isNotEmpty()) {
                                if (newValue.isEmpty()) {
                                    //before return, if the new value is empty, set the text field to empty
                                    otpList[i].value = newValue
                                }

                                return@InputOPTView
                            }
                            //set new value and move cursor to the end
                            otpList[i].value = newValue
                            nextFocus(otpList, requesterList)
                        },
                        focusRequester = requesterList[i]
                    )
                }
            }
        }
    }
    LaunchedEffect(key1 = null, block = {
        delay(300)
        requesterList[0].requestFocus()
    })
}

@Composable
fun InputOPTView(
    value: String,
    onValueChange: (value: String) -> Unit,
    focusRequester: FocusRequester
) {
    BasicTextField(
        readOnly = false,
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .wrapContentSize()
            .focusRequester(focusRequester)
            .border(width = 1.dp, color = AppColor.BorderGrey, RoundedCornerShape(8.dp)),
        maxLines = 1,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        },
        cursorBrush = SolidColor(AppColor.BorderGrey),
        textStyle = TextStyle(
            color = AppColor.Primary,
            fontSize = 26.sp,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = null
        )
    )
}

fun nextFocus(otpList: List<MutableState<String>>, requesterList: List<FocusRequester>) {
    for (index in otpList.indices) {
        if (otpList[index].value == "") {
            if (index < otpList.size) {
                requesterList[index].requestFocus()
                break
            }
        }
    }
}