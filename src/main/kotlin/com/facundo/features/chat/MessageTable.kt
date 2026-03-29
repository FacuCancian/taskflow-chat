package com.facundo.features.chat

import com.facundo.features.auth.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Messages : Table("messages") {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val username = varchar("username", 50)
    val content = text("content")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}