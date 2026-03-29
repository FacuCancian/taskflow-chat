package com.facundo.features.chat

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Long,
    val userId: Long?,
    val username: String,
    val content: String,
    val createdAt: String
)

@Serializable
data class IncomingMessage(
    val content: String
)


@Serializable
data class OutgoingMessage(
    val type: String,
    val username: String,
    val content: String,
    val timestamp: String
)