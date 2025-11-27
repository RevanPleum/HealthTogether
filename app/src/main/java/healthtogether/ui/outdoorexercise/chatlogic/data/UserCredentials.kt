package healthtogether.ui.outdoorexercise.chatlogic.data

import io.getstream.chat.android.client.models.User

data class UserCredentials(
    val apikey: String,
    val user: User,
    val token: String
)
