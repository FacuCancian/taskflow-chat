package com.facundo

import com.facundo.config.JwtConfig
import com.facundo.config.configureCors
import com.facundo.config.configureRouting
import com.facundo.config.configureSecurity
import com.facundo.config.configureSerialization
import com.facundo.config.configureSockets
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
    val port = System.getenv("PORT")?.toInt() ?: 8080

    embeddedServer(
        Netty,
        port = port,
        host = "0.0.0.0"
    ) {
        module()
    }.start(wait = true)
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