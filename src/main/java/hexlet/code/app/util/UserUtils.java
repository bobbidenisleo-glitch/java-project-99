package hexlet.code.app.util;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName();
        if (email == null || email.isEmpty()) {
            return null;
        }

        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean isAuthor(Long userId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return currentUser.getId().equals(userId);
    }

    public boolean isAdmin() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return "hexlet@example.com".equals(currentUser.getEmail());
    }
}
