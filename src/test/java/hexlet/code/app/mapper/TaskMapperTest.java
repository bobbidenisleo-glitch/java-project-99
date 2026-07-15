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

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void testToEntityWithTitleAndContent() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTitle("Title Task");
        dto.setContent("Content Description");
        dto.setIndex(2);

        Task task = taskMapper.toEntity(dto);

        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo("Title Task");
        assertThat(task.getDescription()).isEqualTo("Content Description");
        assertThat(task.getIndex()).isEqualTo(2);
    }

    @Test
    void testToEntityWithTaskStatusId() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Task with Status");
        dto.setTaskStatusId(testStatus.getId());

        Task task = taskMapper.toEntity(dto);

        assertThat(task).isNotNull();
        assertThat(task.getTaskStatus()).isNotNull();
        assertThat(task.getTaskStatus().getId()).isEqualTo(testStatus.getId());
    }

    @Test
    void testToEntityWithStatusSlug() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Task with Status Slug");
        dto.setStatus("in_progress");

        Task task = taskMapper.toEntity(dto);

        assertThat(task).isNotNull();
        assertThat(task.getTaskStatus()).isNotNull();
        assertThat(task.getTaskStatus().getSlug()).isEqualTo("in_progress");
    }

    @Test
    void testToEntityWithAssigneeId() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Task with Assignee");
        dto.setAssigneeId(testUser.getId());

        Task task = taskMapper.toEntity(dto);

        assertThat(task).isNotNull();
        assertThat(task.getAssignee()).isNotNull();
        assertThat(task.getAssignee().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void testToEntityWithLabelIds() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Task with Labels");
        dto.setLabelIds(List.of(testLabel.getId()));

        Task task = taskMapper.toEntity(dto);

        assertThat(task).isNotNull();
        assertThat(task.getLabels()).isNotNull();
        assertThat(task.getLabels()).hasSize(1);
        assertThat(task.getLabels()).extracting("id").contains(testLabel.getId());
    }

    @Test
    void testToEntityWithTaskLabelIds() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Task with TaskLabelIds");
        dto.setTaskLabelIds(List.of(testLabel.getId()));

        Task task = taskMapper.toEntity(dto);

        assertThat(task).isNotNull();
        assertThat(task.getLabels()).isNotNull();
        assertThat(task.getLabels()).hasSize(1);
        assertThat(task.getLabels()).extracting("id").contains(testLabel.getId());
    }

    @Test
    void testToEntityWithLabels() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Task with Labels Field");
        dto.setLabels(List.of(testLabel.getId()));

        Task task = taskMapper.toEntity(dto);

        assertThat(task).isNotNull();
        assertThat(task.getLabels()).isNotNull();
        assertThat(task.getLabels()).hasSize(1);
        assertThat(task.getLabels()).extracting("id").contains(testLabel.getId());
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
        assertThat(dto.getTitle()).isEqualTo("Test Task");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getContent()).isEqualTo("Test Description");
        assertThat(dto.getIndex()).isEqualTo(1);
        assertThat(dto.getStatus()).isEqualTo("in_progress");
        assertThat(dto.getTaskStatusId()).isEqualTo(testStatus.getId());
        assertThat(dto.getAssigneeId()).isEqualTo(testUser.getId());
        assertThat(dto.getLabelIds()).contains(testLabel.getId());
        assertThat(dto.getTaskLabelIds()).contains(testLabel.getId());
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
        assertThat(dto.getLabelIds()).isEmpty();
        assertThat(dto.getTaskLabelIds()).isEmpty();
    }

    @Test
    void testToEntityWithInvalidStatusId() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Task with Invalid Status");
        dto.setTaskStatusId(999L);

        assertThatThrownBy(() -> taskMapper.toEntity(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("TaskStatus not found");
    }

    @Test
    void testToEntityWithInvalidStatusSlug() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Task with Invalid Slug");
        dto.setStatus("invalid_slug");

        assertThatThrownBy(() -> taskMapper.toEntity(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Status not found");
    }

    @Test
    void testToEntityWithInvalidAssigneeId() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Task with Invalid Assignee");
        dto.setAssigneeId(999L);

        assertThatThrownBy(() -> taskMapper.toEntity(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }
}
