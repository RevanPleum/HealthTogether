package healthtogether.apiService.serviceInterface

import healthtogether.apiService.config.API_Config
import healthtogether.apiService.model.Dropdowns.BaseDropdownViewModel
import healthtogether.apiService.model.Dropdowns.DropdownBodyViewModel
import healthtogether.apiService.model.ResponseResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IDropdownService {
    @GET("gender/{language}")
    suspend fun gender(@Path("language") language: String): ResponseResult<List<BaseDropdownViewModel>>

    @GET("career/{language}")
    suspend fun career(@Path("language") language: String): ResponseResult<List<BaseDropdownViewModel>>

    @GET("exercise/{language}")
    suspend fun exercise(@Path("language") language: String): ResponseResult<List<BaseDropdownViewModel>>

    @GET("province/{language}")
    suspend fun province(@Path("language") language: String): ResponseResult<List<BaseDropdownViewModel>>

    @GET("district/{provinceId}/{language}")
    suspend fun district(
        @Path("provinceId") provinceId: String,
        @Path("language") language: String
    ): ResponseResult<List<BaseDropdownViewModel>>

    @GET("subDistrict/{districtId}/{language}")
    suspend fun subDistrict(
        @Path("districtId") districtId: String,
        @Path("language") language: String
    ): ResponseResult<List<BaseDropdownViewModel>>

    @POST("place/{language}")
    suspend fun place(
        @Body dropdownBodyViewModel: DropdownBodyViewModel,
        @Path("language") language: String
    ): ResponseResult<List<BaseDropdownViewModel>>

    companion object {
        const val base_url = API_Config.BASE_URL + "user/SelectLists/"
    }
}