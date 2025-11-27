package healthtogether.ui.fogetPassword.forgetPasswordViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import healthtogether.apiService.service.AccountService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The [ForgetPasswordViewModel] is responsible for controlling the UI logic of the login screen. It will
 * listen for text changes and button clicks, and update the UI state accordingly and expose that so
 * the View can update.
 *
 * Whenever a view action occurs, such as [emailChanged] or [signInButtonClicked], proxy the
 * corresponding [LoginAction] to our [store].
 */
class ForgetPasswordViewModel : ViewModel() {
    private val _isSendSuccess = MutableLiveData<Boolean>()
    val isSendSuccess: LiveData<Boolean> = _isSendSuccess
    fun forgetPassword(userName: String) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response = AccountService.account_API.forgetPassword(userName)
                withContext(Dispatchers.Main) {
                    if (response.isSuccess!!) {
                        _isSendSuccess.value = response.isSuccess
                    } else {
                        var error = response.message
                    }
                }
            } catch (e: Exception) {
                var error = e.message
            }
        }
    }
}