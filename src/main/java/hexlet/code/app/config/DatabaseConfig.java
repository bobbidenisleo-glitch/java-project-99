package hexlet.code.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl == null || dbUrl.isEmpty()) {
            throw new RuntimeException("DATABASE_URL environment variable is not set");
        }

        // Преобразуем postgresql:// в jdbc:postgresql://
        if (dbUrl.startsWith("postgresql://")) {
            dbUrl = dbUrl.replace("postgresql://", "jdbc:postgresql://");
        }

        // Добавляем порт :5432 если его нет
        if (!dbUrl.contains(":")) {
            dbUrl = dbUrl.replace("jdbc:postgresql://", "jdbc:postgresql://");
        }

        URI uri = new URI(dbUrl);
        String userInfo = uri.getUserInfo();
        String username = userInfo.split(":")[0];
        String password = userInfo.split(":")[1];
        String host = uri.getHost();
        String path = uri.getPath();

        // Собираем правильный JDBC URL с портом
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d%s", host, 5432, path);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
