package hexlet.code.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
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
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        labelRepository.deleteAll();

        testLabel = new Label();
        testLabel.setName("Test Label");
        labelRepository.save(testLabel);
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testGetAllLabels() throws Exception {
        mockMvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Label"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testGetLabelById() throws Exception {
        mockMvc.perform(get("/api/labels/" + testLabel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Label"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testCreateLabel() throws Exception {
        Label newLabel = new Label();
        newLabel.setName("New Label");

        mockMvc.perform(post("/api/labels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLabel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Label"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testUpdateLabel() throws Exception {
        Label updateData = new Label();
        updateData.setName("Updated Label");

        mockMvc.perform(put("/api/labels/" + testLabel.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Label"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testDeleteLabel() throws Exception {
        mockMvc.perform(delete("/api/labels/" + testLabel.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
