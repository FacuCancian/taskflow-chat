import com.facundo.features.auth.Users
import com.facundo.features.chat.Messages
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = System.getenv("DATABASE_URL")
                ?: config.property("database.url").getString()
            driverClassName = "org.postgresql.Driver"
            username = System.getenv("DATABASE_USER")
                ?: config.property("database.user").getString()
            password = System.getenv("DATABASE_PASSWORD")
                ?: config.property("database.password").getString()
            maximumPoolSize = 10
            isAutoCommit = false
        }

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.createMissingTablesAndColumns(Users, Messages)
        }
    }
}