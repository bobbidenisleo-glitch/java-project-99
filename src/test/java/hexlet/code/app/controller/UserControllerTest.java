package hexlet.code.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("testpass");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        userRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("newpass");
        newUser.setFirstName("New");
        newUser.setLastName("User");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testUpdateUser() throws Exception {
        User updateData = new User();
        updateData.setFirstName("UpdatedName");

        mockMvc.perform(put("/api/users/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("UpdatedName"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testCreateUserWithInvalidEmail() throws Exception {
        User invalidUser = new User();
        invalidUser.setEmail("invalid-email");
        invalidUser.setPassword("123");
        invalidUser.setFirstName("Invalid");
        invalidUser.setLastName("User");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    public void testCreateUserWithShortPassword() throws Exception {
        User invalidUser = new User();
        invalidUser.setEmail("valid@example.com");
        invalidUser.setPassword("12");
        invalidUser.setFirstName("Invalid");
        invalidUser.setLastName("User");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }
}
