package com.facundo.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("jwt-auth") {
            realm = "taskflow-chat"
            verifier(
                JWT.require(Algorithm.HMAC256(JwtConfig.secret))
                    .withIssuer(JwtConfig.issuer)
                    .withIssuer("taskflow-chat")
                    .withAudience("taskflow-users")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != null)
                    JWTPrincipal(credential.payload)
                else null
            }
        }
    }
}