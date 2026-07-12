package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public Task toEntity(TaskCreateDTO dto) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setIndex(dto.getIndex());

        if (dto.getTaskStatusId() != null) {
            TaskStatus status = taskStatusRepository.findById(dto.getTaskStatusId())
                    .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
            task.setTaskStatus(status);
        }

        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignee(assignee);
        }

        if (dto.getLabelIds() != null && !dto.getLabelIds().isEmpty()) {
            List<Label> labels = labelRepository.findAllById(dto.getLabelIds());
            task.setLabels(labels);
        }

        return task;
    }

    public TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setTitle(task.getName());
        dto.setDescription(task.getDescription());
        dto.setContent(task.getDescription());
        dto.setIndex(task.getIndex());
        dto.setCreatedAt(task.getCreatedAt());

        // status — это слаг статуса
        dto.setStatus(task.getTaskStatus() != null ? task.getTaskStatus().getSlug() : null);

        if (task.getTaskStatus() != null) {
            dto.setTaskStatusId(task.getTaskStatus().getId());
        }
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }
        if (task.getLabels() != null && !task.getLabels().isEmpty()) {
            dto.setLabelIds(task.getLabels().stream()
                    .map(Label::getId)
                    .collect(Collectors.toList()));
        } else {
            dto.setLabelIds(new ArrayList<>());
        }

        return dto;
    }
}
