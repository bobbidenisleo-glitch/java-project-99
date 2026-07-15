package hexlet.code.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Task testTask;
    private TaskStatus testStatus;
    private User testUser;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();

        testStatus = new TaskStatus();
        testStatus.setName("Test Status");
        testStatus.setSlug("test_status");
        taskStatusRepository.save(testStatus);

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("testpass");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        userRepository.save(testUser);

        testTask = new Task();
        testTask.setName("Test Task");
        testTask.setDescription("Test Description");
        testTask.setIndex(1);
        testTask.setTaskStatus(testStatus);
        testTask.setAssignee(testUser);
        taskRepository.save(testTask);
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Task"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testGetTaskById() throws Exception {
        mockMvc.perform(get("/api/tasks/" + testTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testCreateTask() throws Exception {
        Map<String, Object> newTask = new HashMap<>();
        newTask.put("name", "New Task");
        newTask.put("description", "New Description");
        newTask.put("index", 2);
        newTask.put("taskStatusId", testStatus.getId());
        newTask.put("assigneeId", testUser.getId());

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Task"))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testUpdateTask() throws Exception {
    Map<String, Object> updateData = new HashMap<>();
    updateData.put("name", "Updated Task");
    updateData.put("description", "Updated Description");
    updateData.put("taskStatusId", testStatus.getId());
    updateData.put("assigneeId", testUser.getId());

    mockMvc.perform(put("/api/tasks/" + testTask.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateData)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Task"))
            .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
