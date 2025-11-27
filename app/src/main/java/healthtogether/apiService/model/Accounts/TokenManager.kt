package healthtogether.apiService.model.Accounts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TokenManager {
    var token: String by mutableStateOf("")
}