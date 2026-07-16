package hexlet.code.app.util;

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
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUtilsTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserUtils userUtils;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
    }

    @Test
    void testGetCurrentUser_WhenAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        User result = userUtils.getCurrentUser();

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testGetCurrentUser_WhenNotAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User result = userUtils.getCurrentUser();

        assertThat(result).isNull();
    }

    @Test
    void testGetCurrentUser_WhenAuthenticationIsNull() {
        SecurityContextHolder.getContext().setAuthentication(null);

        User result = userUtils.getCurrentUser();

        assertThat(result).isNull();
    }

    @Test
    void testGetCurrentUser_WhenEmailIsEmpty() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User result = userUtils.getCurrentUser();

        assertThat(result).isNull();
    }

    @Test
    void testGetCurrentUser_WhenUserNotFound() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("nonexistent@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        User result = userUtils.getCurrentUser();

        assertThat(result).isNull();
    }

    @Test
    void testIsAuthor_WhenCurrentUserMatches() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        boolean result = userUtils.isAuthor(1L);

        assertThat(result).isTrue();
    }

    @Test
    void testIsAuthor_WhenCurrentUserDoesNotMatch() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        boolean result = userUtils.isAuthor(2L);

        assertThat(result).isFalse();
    }

    @Test
    void testIsAuthor_WhenCurrentUserIsNull() {
        SecurityContextHolder.getContext().setAuthentication(null);

        boolean result = userUtils.isAuthor(1L);

        assertThat(result).isFalse();
    }

    @Test
    void testIsAdmin_WhenUserIsAdmin() {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("hexlet@example.com");

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("hexlet@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail("hexlet@example.com")).thenReturn(Optional.of(adminUser));

        boolean result = userUtils.isAdmin();

        assertThat(result).isTrue();
    }

    @Test
    void testIsAdmin_WhenUserIsNotAdmin() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        boolean result = userUtils.isAdmin();

        assertThat(result).isFalse();
    }

    @Test
    void testIsAdmin_WhenCurrentUserIsNull() {
        SecurityContextHolder.getContext().setAuthentication(null);

        boolean result = userUtils.isAdmin();

        assertThat(result).isFalse();
    }
}
