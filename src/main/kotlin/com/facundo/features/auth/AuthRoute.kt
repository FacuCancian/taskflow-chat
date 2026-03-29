package com.facundo.features.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date
fun Route.authRoutes() {

    post("/auth/register") {
        val body = call.receive<RegisterRequest>()

        // Validación básica
        if (body.username.isBlank() || body.email.isBlank() || body.password.length < 6) {
            return@post call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Datos inválidos")
            )
        }


        val passwordHash = BCrypt.withDefaults()
            .hashToString(12, body.password.toCharArray())

        val userId = try {
            transaction {
                Users.insert {
                    it[username] = body.username.trim()
                    it[email] = body.email.trim().lowercase()
                    it[Users.passwordHash] = passwordHash
                }[Users.id]
            }
        } catch (e: Exception) {
            return@post call.respond(
                HttpStatusCode.Conflict,
                mapOf("error" to "El usuario o email ya existe")
            )
        }

        val token = JWT.create()
            .withIssuer("taskflow-chat")
            .withAudience("taskflow-users")
            .withClaim("userId", userId)
            .withClaim("username", body.username.trim())
            .withExpiresAt(Date(System.currentTimeMillis() + 86_400_000L))
            .sign(Algorithm.HMAC256("secreto-local-cambiar-en-produccion"))

        call.respond(
            HttpStatusCode.Created,
            AuthResponse(
                token = token,
                user = UserDto(id = userId, username = body.username.trim(), email = body.email.trim())
            )
        )
    }
    post("/auth/login") {
        val body = call.receive<LoginRequest>()


        val row = transaction {
            Users.selectAll().where { Users.email eq body.email.lowercase() }
                .singleOrNull()
        } ?: return@post call.respond(
            HttpStatusCode.Unauthorized,
            mapOf("error" to "Credenciales inválidas")
        )


        val isValid = BCrypt.verifyer()
            .verify(body.password.toCharArray(), row[Users.passwordHash])
            .verified

        if (!isValid) {
            return@post call.respond(
                HttpStatusCode.Unauthorized,
                mapOf("error" to "Credenciales inválidas")
            )
        }

        val token = JWT.create()
            .withIssuer("taskflow-chat")
            .withAudience("taskflow-users")
            .withClaim("userId", row[Users.id])
            .withClaim("username", row[Users.username])
            .withExpiresAt(Date(System.currentTimeMillis() + 86_400_000L))
            .sign(Algorithm.HMAC256("secreto-local-cambiar-en-produccion"))

        call.respond(
            AuthResponse(
                token = token,
                user = UserDto(
                    id = row[Users.id],
                    username = row[Users.username],
                    email = row[Users.email]
                )
            )
        )
    }
}