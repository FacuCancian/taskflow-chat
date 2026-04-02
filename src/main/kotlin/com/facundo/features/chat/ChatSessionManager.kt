package com.facundo.features.chat

import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

object ChatSessionManager {

    private val sessions = ConcurrentHashMap<String, WebSocketSession>()
    private val mutex = Mutex()

    suspend fun join(sessionId: String, session: WebSocketSession) {
        mutex.withLock {
            sessions[sessionId] = session
        }
    }

    suspend fun leave(sessionId: String) {
        mutex.withLock {
            sessions.remove(sessionId)
        }
    }

    suspend fun broadcast(message: OutgoingMessage) {
        val json = Json.encodeToString(message)
        val activeSessions = mutex.withLock {
            sessions.values.toList()
        }
        for (session in activeSessions) {
            try {
                session.send(Frame.Text(json))
            } catch (e: Exception) {

            }
        }
    }
}