package healthtogether.apiService.config

import healthtogether.apiService.model.Accounts.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class DynamicHeaderInterceptor(
    private val headers: Map<String, String>? = null,
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Create a new request builder with the original request
        val requestBuilder = originalRequest.newBuilder()

        // Add headers from the object to the request builder
        if (!headers.isNullOrEmpty()) {
            for ((key, value) in headers) {
                requestBuilder.addHeader(key, value)
            }
        }
        val token = tokenManager.token
        if (token.isNotEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        // Add Content-Type header with application/json value
        requestBuilder.header("Content-Type", "application/json")

        // Build the new request with added headers
        val newRequest = requestBuilder.build()

        // Proceed with the request chain
        return chain.proceed(newRequest)
    }
}