package healthtogether.ui.login.loginmodel

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

fun CreateToken(userID: String, apiKey: String): String {
    return Jwts.builder()
        .setHeaderParam("alg", "HS256")
        .setHeaderParam("typ", "JWT")
        .claim("user_id", "$userID")
        .signWith(SignatureAlgorithm.HS256, "$apiKey".toByteArray())
        .compact()
}