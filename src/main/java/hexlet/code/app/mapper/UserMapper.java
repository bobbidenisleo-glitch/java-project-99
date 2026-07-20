package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
