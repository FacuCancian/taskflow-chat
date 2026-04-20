package com.facundo.config

import io.ktor.server.config.ApplicationConfig

object JwtConfig {
    lateinit var secret: String
    lateinit var issuer: String
    lateinit var audience: String
    lateinit var realm: String

    fun init(config: ApplicationConfig) {
        secret   = config.property("jwt.secret").getString()
        issuer   = config.property("jwt.issuer").getString()
        audience = config.property("jwt.audience").getString()
        realm    = config.property("jwt.realm").getString()
    }
}