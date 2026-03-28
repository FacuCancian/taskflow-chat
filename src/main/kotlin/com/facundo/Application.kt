package com.facundo

import com.facundo.config.configureRouting
import com.facundo.config.configureSecurity
import com.facundo.config.configureSerialization
import com.facundo.config.configureSockets
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init(environment.config)
    configureSecurity()
    configureSockets()
    configureSerialization()
    configureRouting()
}