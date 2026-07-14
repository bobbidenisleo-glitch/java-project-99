package hexlet.code;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
public class AppApplication {

    public static void main(String[] args) {
        String dsn = System.getenv("SENTRY_DSN");
        if (dsn != null && !dsn.isEmpty()) {
            Sentry.init(options -> {
                options.setDsn(dsn);
                options.setSendDefaultPii(true);
                options.setDebug(true);
            });
            log.info("Sentry initialized successfully");
        } else {
            log.info("Sentry DSN not found, skipping Sentry initialization");
        }

        SpringApplication.run(AppApplication.class, args);
    }
}
