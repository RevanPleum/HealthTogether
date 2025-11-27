package healthtogether.ui.login

import healthtogether.redux.Middleware
import healthtogether.redux.Store
import healthtogether.ui.login.loginmodel.LoginAction
import healthtogether.ui.login.loginmodel.LoginViewState

class LoginNetworkingMiddleware(
    private val loginRepository: LoginRepository,
) : Middleware<LoginViewState, LoginAction> {

    override suspend fun process(
        action: LoginAction,
        currentState: LoginViewState,
        store: Store<LoginViewState, LoginAction>,
    ) {
        when (action) {
            is LoginAction.SignInButtonClicked -> {
                if (currentState.email.isEmpty()) {
                    store.dispatch(LoginAction.InvalidEmailSubmitted)
                    return
                }

                loginUser(store, currentState)
            }

            else -> {}
        }
    }

    private suspend fun loginUser(
        store: Store<LoginViewState, LoginAction>,
        currentState: LoginViewState
    ) {
        store.dispatch(LoginAction.LoginStarted)

        val isSuccessful = loginRepository.login(
            email = currentState.email,
            password = currentState.password,
        )

        if (isSuccessful) {
            store.dispatch(LoginAction.LoginCompleted)
        } else {
            store.dispatch(LoginAction.LoginFailed(null))
        }
    }
}