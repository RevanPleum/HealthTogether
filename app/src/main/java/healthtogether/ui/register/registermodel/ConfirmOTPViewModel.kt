package healthtogether.ui.register.registermodel

import android.os.CountDownTimer
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import healthtogether.apiService.model.Accounts.RequestConfirmEmailViewModel
import healthtogether.apiService.model.Accounts.ResponseCreateChatViewModel
import healthtogether.apiService.model.Accounts.ResponseCreateUserViewModel
import healthtogether.apiService.service.AccountService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class ConfirmOTPViewModel : ViewModel() {
    private val _tokenAndOTP = MutableLiveData<ResponseCreateUserViewModel>()
    val tokenAndOTP: LiveData<ResponseCreateUserViewModel> = _tokenAndOTP

    private val _isConfirmSuccess = MutableLiveData<Boolean>()
    val isConfirmSuccess: LiveData<Boolean> = _isConfirmSuccess

    private val _isChatIdSuccess = MutableLiveData<Boolean>()
    val isChatIdSuccess: LiveData<Boolean> = _isChatIdSuccess

    private val _responseChatSuccess = MutableLiveData<ResponseCreateChatViewModel>()
    val responseChatSuccess: LiveData<ResponseCreateChatViewModel> = _responseChatSuccess

    fun UpdateData(dataJson: ResponseCreateUserViewModel) {
        _tokenAndOTP.value = dataJson
    }

    fun comfirmEmail(request: RequestConfirmEmailViewModel) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                AccountService.updateToken(tokenAndOTP.value?.tokenAuth)
                val response = AccountService.account_API.confirmEmail(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccess!!) {
                        _isConfirmSuccess.value = true
                    } else {
                        var error = response.message
                    }
                }
            } catch (e: Exception) {
                var error = e.message
            }
        }
    }

//    fun saveChatId(request: RequestChatIdViewModel) {
//        viewModelScope.launch(Dispatchers.Default) {
//            try {
//                AccountService.updateToken(tokenAndOTP.value?.tokenAuth)
//                val response = AccountService.account_API.saveChatId(request)
//
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccess!!) {
//                        _isChatIdSuccess.value = true
//                    } else {
//                        var error = response.message
//                    }
//                }
//            } catch (e: Exception) {
//                var error = e.message
//            }
//        }
//    }
//
//    fun RegistrationChat(userName:String) {
//        val user = User(id = userName)
//        val jwt = CreateToken(userName)
//        _responseChatSuccess.value = ResponseCreateChatViewModel(true, userName, jwt)
//    }
//
//    private fun CreateToken(userID: String): String {
//        return Jwts.builder()
//            .setHeaderParam("alg", "HS256")
//            .setHeaderParam("typ", "JWT")
//            .claim("user_id", userID)
//            .signWith(SignatureAlgorithm.HS256, apiKey.toByteArray())
//            .compact()
//    }

    private var countDownTimer: CountDownTimer? = null

    private val userInputMinute = TimeUnit.MINUTES.toMillis(2)
    private val userInputSecond = TimeUnit.SECONDS.toMillis(0)

    private val initialTotalTimeInMillis = userInputMinute + userInputSecond
    var timeLeft = mutableLongStateOf(initialTotalTimeInMillis)
    val countDownInterval = 1000L // 1 seconds is the lowest

    val isPlaying = mutableStateOf(false)

    fun startCountDownTimer() = viewModelScope.launch {
        isPlaying.value = true
        countDownTimer = object : CountDownTimer(timeLeft.longValue, countDownInterval) {
            override fun onTick(currentTimeLeft: Long) {
                timeLeft.longValue = currentTimeLeft
            }

            override fun onFinish() {
                isPlaying.value = false
            }
        }.start()
    }

    fun stopCountDownTimer() = viewModelScope.launch {
        isPlaying.value = false
        countDownTimer?.cancel()
    }

    fun resetCountDownTimer() = viewModelScope.launch {
        isPlaying.value = false
        countDownTimer?.cancel()
        timeLeft.longValue = initialTotalTimeInMillis
    }
}