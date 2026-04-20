package com.facundo

import com.facundo.config.JwtConfig
import com.facundo.config.configureCors
import com.facundo.config.configureRouting
import com.facundo.config.configureSecurity
import com.facundo.config.configureSerialization
import com.facundo.config.configureSockets
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    JwtConfig.init(environment.config)
    DatabaseFactory.init(environment.config)
    DatabaseFactory.init(environment.config)
    configureCors()
    configureSecurity()
    configureSockets()
    configureSerialization()
    configureRouting()
}