package hexlet.code;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AppApplication {

    public static void main(String[] args) {
        // Инициализация Sentry только если DSN задан
        String dsn = System.getenv("SENTRY_DSN");
        if (dsn != null && !dsn.isEmpty()) {
            Sentry.init(options -> {
                options.setDsn(dsn);
                options.setSendDefaultPii(true);
                options.setDebug(true);
            });
            System.out.println("Sentry initialized successfully");
        } else {
            System.out.println("Sentry DSN not found, skipping Sentry initialization");
        }

        SpringApplication.run(AppApplication.class, args);
    }

}
