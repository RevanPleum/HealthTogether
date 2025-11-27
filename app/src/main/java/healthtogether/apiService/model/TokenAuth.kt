package healthtogether.apiService.model

data class TokenAuth(
    val isAuthenticated: Boolean? = null,
    val userName: String? = null,
    val tokenAuth: String? = null,
    val userStatus: String? = null,
    val chatId: String? = null,
    val userId: String? = null
)
