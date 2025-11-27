package healthtogether.apiService.model.Accounts

data class ResponseCreateUserViewModel(
    val otp: String? = null,
    val refOTP: String? = null,
    val isAuthenticated: Boolean? = null,
    val userName: String? = null,
    val tokenAuth: String? = null,
    val userStatus: String? = null
)