package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    private TaskStatus testStatus;
    private User testUser;
    private Label testLabel;
    private Task testTask;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();

        testStatus = new TaskStatus();
        testStatus.setName("In Progress");
        testStatus.setSlug("in_progress");
        taskStatusRepository.save(testStatus);

        testUser = new User();
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        userRepository.save(testUser);

        testLabel = new Label();
        testLabel.setName("Bug");
        labelRepository.save(testLabel);

        testTask = new Task();
        testTask.setName("Test Task");
        testTask.setDescription("Test Description");
        testTask.setIndex(1);
        testTask.setTaskStatus(testStatus);
        testTask.setAssignee(testUser);
        taskRepository.save(testTask);
    }

    @Test
    void testGetAllTasksWithoutFilters() {
        List<TaskDTO> tasks = taskService.getAllTasks(null, null, null, null);
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getName()).isEqualTo("Test Task");
    }

    @Test
    void testGetAllTasksWithTitleFilter() {
        List<TaskDTO> tasks = taskService.getAllTasks("Test", null, null, null);
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);

        List<TaskDTO> emptyTasks = taskService.getAllTasks("NonExistent", null, null, null);
        assertThat(emptyTasks).isEmpty();
    }

    @Test
    void testGetAllTasksWithAssigneeFilter() {
        List<TaskDTO> tasks = taskService.getAllTasks(null, testUser.getId(), null, null);
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);

        List<TaskDTO> emptyTasks = taskService.getAllTasks(null, 999L, null, null);
        assertThat(emptyTasks).isEmpty();
    }

    @Test
    void testGetAllTasksWithStatusFilter() {
        List<TaskDTO> tasks = taskService.getAllTasks(null, null, "in_progress", null);
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);

        List<TaskDTO> emptyTasks = taskService.getAllTasks(null, null, "invalid_status", null);
        assertThat(emptyTasks).isEmpty();
    }

    @Test
    void testUpdateTaskNameAndDescription() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Updated Task Name");
        dto.setDescription("Updated Description");

        TaskDTO updated = taskService.updateTask(testTask.getId(), dto);

        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("Updated Task Name");
        assertThat(updated.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void testUpdateTaskTitle() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTitle("Title Update");

        TaskDTO updated = taskService.updateTask(testTask.getId(), dto);

        assertThat(updated.getName()).isEqualTo("Title Update");
    }

    @Test
    void testUpdateTaskContent() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setContent("Content Update");

        TaskDTO updated = taskService.updateTask(testTask.getId(), dto);

        assertThat(updated.getDescription()).isEqualTo("Content Update");
    }

    @Test
    void testUpdateTaskIndex() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setIndex(10);

        TaskDTO updated = taskService.updateTask(testTask.getId(), dto);

        assertThat(updated.getIndex()).isEqualTo(10);
    }

    @Test
    void testUpdateTaskStatusById() {
        TaskStatus newStatus = new TaskStatus();
        newStatus.setName("Done");
        newStatus.setSlug("done");
        taskStatusRepository.save(newStatus);

        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTaskStatusId(newStatus.getId());

        TaskDTO updated = taskService.updateTask(testTask.getId(), dto);

        assertThat(updated.getTaskStatusId()).isEqualTo(newStatus.getId());
        assertThat(updated.getStatus()).isEqualTo("done");
    }

    @Test
    void testUpdateTaskStatusBySlug() {
        TaskStatus newStatus = new TaskStatus();
        newStatus.setName("Done");
        newStatus.setSlug("done");
        taskStatusRepository.save(newStatus);

        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setStatus("done");

        TaskDTO updated = taskService.updateTask(testTask.getId(), dto);

        assertThat(updated.getStatus()).isEqualTo("done");
    }

    @Test
    void testUpdateTaskStatusDirect() {
        TaskStatus newStatus = new TaskStatus();
        newStatus.setName("Done");
        newStatus.setSlug("done");
        taskStatusRepository.save(newStatus);

        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTaskStatus(newStatus);

        TaskDTO updated = taskService.updateTask(testTask.getId(), dto);

        assertThat(updated.getTaskStatusId()).isEqualTo(newStatus.getId());
    }

    @Test
    void testUpdateTaskAssigneeById() {
        User newUser = new User();
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");
        newUser.setFirstName("New");
        newUser.setLastName("User");
        userRepository.save(newUser);

        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setAssigneeId(newUser.getId());

        TaskDTO updated = taskService.updateTask(testTask.getId(), dto);

        assertThat(updated.getAssigneeId()).isEqualTo(newUser.getId());
    }

    @Test
    void testUpdateTaskAssigneeDirect() {
        User newUser = new User();
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");
        newUser.setFirstName("New");
        newUser.setLastName("User");
        userRepository.save(newUser);

        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setAssignee(newUser);

        TaskDTO updated = taskService.updateTask(testTask.getId(), dto);

        assertThat(updated.getAssigneeId()).isEqualTo(newUser.getId());
    }

    @Test
    void testUpdateTaskNotFound() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setName("Any Name");

        assertThatThrownBy(() -> taskService.updateTask(999L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void testUpdateTaskStatusNotFound() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTaskStatusId(999L);

        assertThatThrownBy(() -> taskService.updateTask(testTask.getId(), dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("TaskStatus not found");
    }

    @Test
    void testUpdateTaskStatusBySlugNotFound() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setStatus("invalid_slug");

        assertThatThrownBy(() -> taskService.updateTask(testTask.getId(), dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Status not found");
    }

    @Test
    void testUpdateTaskUserNotFound() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setAssigneeId(999L);

        assertThatThrownBy(() -> taskService.updateTask(testTask.getId(), dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }
}
