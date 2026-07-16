package hexlet.code.app.service;

import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<UserDTO> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("test@example.com");
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserDTO result = userService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDTO result = userService.createUser(testUser);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void testUpdateUser_Success() {
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("Name");
        updatedUser.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDTO result = userService.updateUser(1L, updatedUser);

        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(999L, new User()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testUpdateUser_NotOwner() {
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(authentication.getName()).thenReturn("other@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThatThrownBy(() -> userService.updateUser(1L, updatedUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("You can only edit your own profile");
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setEmail("other@example.com");

        User updatedUser = new User();
        updatedUser.setEmail("other@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userService.updateUser(1L, updatedUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }
}
