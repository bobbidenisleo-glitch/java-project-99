package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;

    public Task toEntity(TaskCreateDTO dto) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setIndex(dto.getIndex());

        // Устанавливаем статус
        if (dto.getTaskStatusId() != null) {
            TaskStatus status = taskStatusRepository.findById(dto.getTaskStatusId())
                    .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
            task.setTaskStatus(status);
        }

        // Устанавливаем исполнителя
        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignee(assignee);
        }

        return task;
    }

    public TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setIndex(task.getIndex());
        dto.setCreatedAt(task.getCreatedAt());

        if (task.getTaskStatus() != null) {
            dto.setTaskStatusId(task.getTaskStatus().getId());
        }
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }

        return dto;
    }
}
