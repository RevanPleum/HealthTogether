package healthtogether.ui.outdoorexercise.chatlogic.data

import android.os.Build
import androidx.annotation.RequiresApi
import io.getstream.chat.android.client.models.User

object TestDefinedUser {

    const val API_KEY: String = "v2cfg3r2wadr"

    @RequiresApi(Build.VERSION_CODES.O)
    val activeUser: List<UserCredentials> = listOf(
        UserCredentials(
            apikey = API_KEY,
            user = User(
                id = "PleumAdmin001",
                name = "PleumAdmin001",
                image = "https://i.pximg.net/img-original/img/2024/05/07/20/18/11/118524853_p1.png"
            ),
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiUGxldW1BZG1pbjAwMSJ9._0p96jz-xX3Km6FgGe1k64oDiibR89ho4OimOpRCU2U"
        )
    )

}