package com.facundo.features.chat

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID
import io.ktor.server.response.*
fun Route.chatRoutes() {

    // Historial de mensajes
    get("/messages") {
        val messages = transaction {
            Messages.selectAll()
                .orderBy(Messages.createdAt)
                .map {
                    MessageDto(
                        id = it[Messages.id],
                        userId = it[Messages.userId],
                        username = it[Messages.username],
                        content = it[Messages.content],
                        createdAt = it[Messages.createdAt].toString()
                    )
                }
        }
        call.respond(messages)
    }

    // WebSocket
    webSocket("/chat/ws") {
        val sessionId = UUID.randomUUID().toString()
        val username = call.request.queryParameters["username"] ?: "anonimo"

        ChatSessionManager.join(sessionId, this)

        ChatSessionManager.broadcast(
            OutgoingMessage(
                type = "joined",
                username = username,
                content = "$username se unió al chat",
                timestamp = LocalDateTime.now().toString()
            )
        )

        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    val incoming = Json.decodeFromString<IncomingMessage>(text)

                    if (incoming.content.isBlank()) return@consumeEach


                    transaction {
                        Messages.insert {
                            it[userId] = null
                            it[Messages.username] = username
                            it[content] = incoming.content.trim()
                        }
                    }

                    ChatSessionManager.broadcast(
                        OutgoingMessage(
                            type = "message",
                            username = username,
                            content = incoming.content.trim(),
                            timestamp = LocalDateTime.now().toString()
                        )
                    )
                }
            }
        } finally {
            ChatSessionManager.leave(sessionId)
            ChatSessionManager.broadcast(
                OutgoingMessage(
                    type = "left",
                    username = username,
                    content = "$username salió del chat",
                    timestamp = LocalDateTime.now().toString()
                )
            )
        }
    }
}