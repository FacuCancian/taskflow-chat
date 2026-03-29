package com.facundo.config

import com.facundo.features.auth.authRoutes
import com.facundo.features.chat.chatRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoutes()
        chatRoutes()
    }
}