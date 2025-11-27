package healthtogether.apiService.service

import healthtogether.apiService.config.API_Config
import healthtogether.apiService.config.DynamicHeaderInterceptor
import healthtogether.apiService.model.Accounts.TokenManager
import healthtogether.apiService.serviceInterface.IDropdownService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DropdownService {
    private val tokenManager = TokenManager()

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Add any other custom configurations if needed
    private val headers = mapOf("x-api-key" to API_Config.X_API_KEY)

    val client = OkHttpClient.Builder()
        .addInterceptor(DynamicHeaderInterceptor(headers, tokenManager))
        .addInterceptor(loggingInterceptor)
        .build()

    val dropdown_API: IDropdownService = Retrofit.Builder()
        .baseUrl(IDropdownService.base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(IDropdownService::class.java)

    fun updateToken(newToken: String?) {
        tokenManager.token = newToken!!
    }
}
