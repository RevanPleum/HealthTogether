package healthtogether.apiService.model.Accounts

data class RequestLoginViewModel(
    val userName: String? = null,
    val password: String? = null,
)

data class RequestCreateUserViewModel(
    val name: String? = null,
    val userName: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val citizenId: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val gender: String? = null,
    val age: String? = null,
    val category: String? = null,
    val careerId: String? = null,
    val exerciseId: String? = null,
    val salaryRateId: String? = null,
    val otp: String? = null,
    val refOTP: String? = null
)

data class RequestConfirmEmailViewModel(
    val userName: String? = null,
    val otp: String? = null,
    val ref: String? = null
)

data class RequestChatIdViewModel(
    val userName: String? = null,
    val chatId: String? = null
)

data class ResponseCreateChatViewModel(
    val isSuccess: Boolean,
    val userName: String? = null,
    val chatId: String? = null
)