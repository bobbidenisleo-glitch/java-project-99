package hexlet.code.app.service;

import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDTO(user);
    }

    @Override
    public UserDTO createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    @Override
    public UserDTO updateUser(Long id, User updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Проверяем, что пользователь редактирует себя
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!existing.getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("You can only edit your own profile");
        }

        if (updatedUser.getFirstName() != null) {
            existing.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            existing.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getEmail() != null) {
            if (userRepository.findByEmail(updatedUser.getEmail()).isPresent() &&
                !existing.getEmail().equals(updatedUser.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            existing.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        User saved = userRepository.save(existing);
        return toDTO(saved);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
