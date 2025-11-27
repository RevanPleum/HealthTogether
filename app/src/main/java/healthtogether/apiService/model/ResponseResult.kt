package healthtogether.apiService.model

data class ResponseResult<T>(
    val data: T? = null,
    val httpStatusCode: String? = null,
    val message: String? = null,
    val code: String? = null,
    val isSuccess: Boolean? = null
)