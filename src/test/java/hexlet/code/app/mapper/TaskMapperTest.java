package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TaskMapperTest {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    private TaskStatus testStatus;
    private User testUser;
    private Label testLabel;

    @BeforeEach
    void setUp() {
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
        labelRepository.deleteAll();

        testStatus = new TaskStatus();
        testStatus.setName("In Progress");
        testStatus.setSlug("in_progress");
        taskStatusRepository.save(testStatus);

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        userRepository.save(testUser);

        testLabel = new Label();
        testLabel.setName("Bug");
        labelRepository.save(testLabel);
    }

    @Test
    void testToEntityWithNameAndDescription() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Test Task");
        dto.setDescription("Test Description");
        dto.setIndex(1);

        Task task = taskMapper.toEntity(dto);

        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo("Test Task");
        assertThat(task.getDescription()).isEqualTo("Test Description");
        assertThat(task.getIndex()).isEqualTo(1);
    }

    @Test
    void testToDTO() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setDescription("Test Description");
        task.setIndex(1);
        task.setTaskStatus(testStatus);
        task.setAssignee(testUser);
        task.setLabels(Set.of(testLabel));

        TaskDTO dto = taskMapper.toDTO(task);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test Task");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getIndex()).isEqualTo(1);
    }

    @Test
    void testToDTOWithoutLabels() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setDescription("Test Description");
        task.setIndex(1);
        task.setTaskStatus(testStatus);
        task.setAssignee(testUser);

        TaskDTO dto = taskMapper.toDTO(task);

        assertThat(dto).isNotNull();
        assertThat(dto.getLabelIds()).isNull();
        assertThat(dto.getTaskLabelIds()).isNull();
    }
}
