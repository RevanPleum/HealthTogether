package healthtogether.ui.login.loginmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import healthtogether.apiService.model.Accounts.RequestLoginViewModel
import healthtogether.apiService.model.TokenAuth
import healthtogether.apiService.service.AccountService
import healthtogether.redux.Store
import healthtogether.ui.login.LoggingMiddleware
import healthtogether.ui.login.LoginNetworkingMiddleware
import healthtogether.ui.login.ProdLoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The [LoginPageViewModel] is responsible for controlling the UI logic of the login screen. It will
 * listen for text changes and button clicks, and update the UI state accordingly and expose that so
 * the View can update.
 *
 * Whenever a view action occurs, such as [emailChanged] or [signInButtonClicked], proxy the
 * corresponding [LoginAction] to our [store].
 */
class LoginPageViewModel : ViewModel() {
    private val _token = MutableLiveData<TokenAuth>()
    val token: LiveData<TokenAuth> = _token
    fun login(userName: String, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val requestBody = RequestLoginViewModel(userName, password)
                val response = AccountService.account_API.login(requestBody)
                withContext(Dispatchers.Main) {
                    if (response.isSuccess!!) {
                        _token.value = response.data
                    } else {
                        var error = response.message
                    }
                }
            } catch (e: Exception) {
                var error = e.message
            }
        }
    }

    private val store = Store(
        initialState = LoginViewState(),
        reducer = LoginReducer(),
        middlewares = listOf(
            LoggingMiddleware(),
            LoginNetworkingMiddleware(
                loginRepository = ProdLoginService(),
            ),
        )
    )

    val viewState: StateFlow<LoginViewState> = store.state

    fun emailChanged(newEmail: String) {
        val action = LoginAction.EmailChanged(newEmail)

        viewModelScope.launch {
            store.dispatch(action)
        }
    }

    fun passwordChanged(newPassword: String) {
        val action = LoginAction.PasswordChanged(newPassword)

        viewModelScope.launch {
            store.dispatch(action)
        }
    }

    fun signInButtonClicked() {
        val action = LoginAction.SignInButtonClicked

        viewModelScope.launch {
            store.dispatch(action)
        }
    }

    fun scanFaceClick() {
        val action = LoginAction.ScanFace

        viewModelScope.launch {
            store.dispatch(action)
        }
    }
}