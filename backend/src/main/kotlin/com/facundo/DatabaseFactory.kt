import com.facundo.features.auth.Users
import com.facundo.features.chat.Messages
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {

    fun init(config: ApplicationConfig) {

        val databaseUrl = System.getenv("DATABASE_URL")
        val hikariConfig = if (databaseUrl != null) {
            val uri = URI(databaseUrl)

            val username = System.getenv("DATABASE_USER")
            val password = System.getenv("DATABASE_PASSWORD")

            val jdbcUrl =
                "jdbc:postgresql://${uri.host}:${uri.port}/${uri.path}?sslmode=require"

            HikariConfig().apply {
                this.jdbcUrl = jdbcUrl
                this.driverClassName = "org.postgresql.Driver"
                this.username = username
                this.password = password
                this.maximumPoolSize = 10
            }

        } else {
            HikariConfig().apply {
                jdbcUrl = config.property("database.url").getString()
                driverClassName = config.property("database.driver").getString()
                username = config.property("database.user").getString()
                password = config.property("database.password").getString()
                maximumPoolSize = 10
            }
        }

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.createMissingTablesAndColumns(Users, Messages)
        }
    }
}