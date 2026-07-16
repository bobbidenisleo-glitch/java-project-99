package hexlet.code.app.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        // Создаём пользователя для тестов
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFirstName("Test");
        user.setLastName("User");
        userRepository.save(user);
    }

    @Test
    void testBadCredentialsException() throws Exception {
        // Неверный пароль
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "test@example.com",
                        "password": "wrongpassword"
                    }
                    """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testAccessDeniedException() throws Exception {
        // Попытка обновить пользователя без прав администратора
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "firstName": "Updated"
                    }
                    """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Access denied"));
    }
}
