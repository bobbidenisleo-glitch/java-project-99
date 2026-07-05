package hexlet.code.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskStatus testStatus;

    @BeforeEach
    public void setUp() {
        taskStatusRepository.deleteAll();

        testStatus = new TaskStatus();
        testStatus.setName("Test Status");
        testStatus.setSlug("test_status");
        taskStatusRepository.save(testStatus);
    }

    @Test
    @WithMockUser(username = "hexlet@example.com", roles = {"USER"})
    public void testGetAllStatuses() throws Exception {
        mockMvc.perform(get("/api/task_statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Status"))
                .andExpect(jsonPath("$[0].slug").value("test_status"));
    }

    @Test
    @WithMockUser(username = "hexlet@example.com", roles = {"USER"})
    public void testGetStatusById() throws Exception {
        mockMvc.perform(get("/api/task_statuses/" + testStatus.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Status"))
                .andExpect(jsonPath("$.slug").value("test_status"));
    }

    @Test
    @WithMockUser(username = "hexlet@example.com", roles = {"USER"})
    public void testCreateStatus() throws Exception {
        TaskStatus newStatus = new TaskStatus();
        newStatus.setName("New Status");
        newStatus.setSlug("new_status");

        mockMvc.perform(post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStatus)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Status"))
                .andExpect(jsonPath("$.slug").value("new_status"));
    }

    @Test
    @WithMockUser(username = "hexlet@example.com", roles = {"USER"})
    public void testUpdateStatus() throws Exception {
        TaskStatus updateData = new TaskStatus();
        updateData.setName("Updated Status");

        mockMvc.perform(put("/api/task_statuses/" + testStatus.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Status"))
                .andExpect(jsonPath("$.slug").value("test_status"));
    }

    @Test
    @WithMockUser(username = "hexlet@example.com", roles = {"USER"})
    public void testDeleteStatus() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + testStatus.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/task_statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(username = "hexlet@example.com", roles = {"USER"})
    public void testCreateStatusWithDuplicateSlug() throws Exception {
        TaskStatus duplicate = new TaskStatus();
        duplicate.setName("Duplicate Status");
        duplicate.setSlug("test_status");

        mockMvc.perform(post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest());
    }
}
