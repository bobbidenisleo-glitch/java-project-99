package hexlet.code.app;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AppApplication {

    public static void main(String[] args) {
        // Инициализация Sentry
        Sentry.init(options -> {
            options.setDsn(System.getenv("SENTRY_DSN"));
            options.setSendDefaultPii(true);
            options.setDebug(true);
        });

        // Тестовый вызов для проверки Sentry
        try {
            throw new Exception("Test exception from Sentry");
        } catch (Exception e) {
            Sentry.captureException(e);
            System.out.println("Test exception sent to Sentry");
        }

        SpringApplication.run(AppApplication.class, args);
    }

}
