package healthtogether.ui.login.loginmodel

import healthtogether.redux.State

/**
 * An implementation of [State] that describes the configuration of the login screen at a given time.
 */
data class LoginViewState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
) : State