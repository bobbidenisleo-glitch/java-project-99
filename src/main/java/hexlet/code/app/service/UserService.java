package hexlet.code.app.service;

import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.model.User;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO createUser(User user);

    UserDTO updateUser(Long id, User userData);

    void deleteUser(Long id);
}
