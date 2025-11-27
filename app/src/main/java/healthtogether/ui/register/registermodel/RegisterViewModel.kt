package healthtogether.ui.register.registermodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import healthtogether.apiService.model.Accounts.RequestCreateUserViewModel
import healthtogether.apiService.model.Accounts.ResponseCreateUserViewModel
import healthtogether.apiService.model.Dropdowns.BaseDropdownViewModel
import healthtogether.apiService.service.AccountService
import healthtogether.apiService.service.DropdownService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel : ViewModel() {
    private val _tokenAndOTP = MutableLiveData<ResponseCreateUserViewModel>()
    val tokenAndOTP: LiveData<ResponseCreateUserViewModel> = _tokenAndOTP
    private val apiKey = "bdcm9uc3thmtsa8kxeujehtctfmksa4hc8rmns85wze3vjcmbyux7evt2b426gh5"

    fun createUser(request: RequestCreateUserViewModel) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response = AccountService.account_API.createUser(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccess!!) {
                        _tokenAndOTP.value = response.data
                    } else {
                        var error = response.message
                    }
                }
            } catch (e: Exception) {
                var error = e.message
            }
        }
    }

    suspend fun getGenders(): List<BaseDropdownViewModel>? {
        try {
            val response = DropdownService.dropdown_API.gender("TH")
            if (response.isSuccess!!) {
                return response.data
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return emptyList()
    }

    suspend fun getExercises(): List<BaseDropdownViewModel>? {
        try {
            val response = DropdownService.dropdown_API.exercise("TH")
            if (response.isSuccess!!) {
                return response.data
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return emptyList()
    }

    suspend fun getCareers(): List<BaseDropdownViewModel>? {
        try {
            val response = DropdownService.dropdown_API.career("TH")
            if (response.isSuccess!!) {
                return response.data
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return emptyList()
    }
}