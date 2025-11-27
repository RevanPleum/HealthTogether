package healthtogether.apiService.serviceInterface

import healthtogether.apiService.config.API_Config
import healthtogether.apiService.model.Accounts.RequestChatIdViewModel
import healthtogether.apiService.model.Accounts.RequestConfirmEmailViewModel
import healthtogether.apiService.model.Accounts.RequestCreateUserViewModel
import healthtogether.apiService.model.Accounts.RequestLoginViewModel
import healthtogether.apiService.model.Accounts.ResponseCreateUserViewModel
import healthtogether.apiService.model.ResponseResult
import healthtogether.apiService.model.TokenAuth
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IAccountService {
    @POST("login")
    suspend fun login(
        @Body requestLoginViewModel: RequestLoginViewModel
    ): ResponseResult<TokenAuth>

    @POST("create")
    suspend fun createUser(
        @Body createUserViewModel: RequestCreateUserViewModel
    ): ResponseResult<ResponseCreateUserViewModel>


    @POST("confirmEmail")
    suspend fun confirmEmail(
        @Body createUserViewModel: RequestConfirmEmailViewModel
    ): ResponseResult<String>

    @POST("saveChatId")
    suspend fun saveChatId(
        @Body createUserViewModel: RequestChatIdViewModel
    ): ResponseResult<String>

    @GET("forgetPassword/{email}")
    suspend fun forgetPassword(@Path("email") email: String): ResponseResult<String>

    companion object {
        const val base_url = API_Config.BASE_URL + "user/accounts/"
    }
}