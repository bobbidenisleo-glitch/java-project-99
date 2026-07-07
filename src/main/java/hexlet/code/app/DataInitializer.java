package hexlet.code.app;

import hexlet.code.app.model.Label;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Создаём администратора
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("hexlet@example.com");
            admin.setPassword(passwordEncoder.encode("qwerty"));
            admin.setFirstName("Admin");
            admin.setLastName("Hexlet");
            userRepository.save(admin);
            System.out.println("Admin user created: hexlet@example.com / qwerty");
        }

        // Создаём дефолтные статусы
        List<String[]> defaultStatuses = List.of(
                new String[]{"Draft", "draft"},
                new String[]{"To Review", "to_review"},
                new String[]{"To Be Fixed", "to_be_fixed"},
                new String[]{"To Publish", "to_publish"},
                new String[]{"Published", "published"}
        );

        for (String[] statusData : defaultStatuses) {
            String name = statusData[0];
            String slug = statusData[1];
            if (taskStatusRepository.findBySlug(slug).isEmpty()) {
                TaskStatus status = new TaskStatus();
                status.setName(name);
                status.setSlug(slug);
                taskStatusRepository.save(status);
                System.out.println("Default status created: " + name + " (" + slug + ")");
            }
        }

        // Создаём дефолтные метки
        List<String> defaultLabels = List.of("feature", "bug");

        for (String labelName : defaultLabels) {
            if (labelRepository.findByName(labelName).isEmpty()) {
                Label label = new Label();
                label.setName(labelName);
                labelRepository.save(label);
                System.out.println("Default label created: " + labelName);
            }
        }
    }
}
