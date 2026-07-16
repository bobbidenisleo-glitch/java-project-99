package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "labels", ignore = true)
    Task toEntity(TaskCreateDTO dto);

    TaskDTO toDTO(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "labels", ignore = true)
    void updateEntity(TaskCreateDTO dto, @MappingTarget Task task);
}
