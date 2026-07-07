package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return toDTO(task);
    }

    public TaskDTO createTask(Task task) {
    // Проверяем и устанавливаем статус
    if (task.getTaskStatus() != null && task.getTaskStatus().getId() != null) {
        TaskStatus status = taskStatusRepository.findById(task.getTaskStatus().getId())
                .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
        task.setTaskStatus(status);
    }

    // Проверяем и устанавливаем исполнителя
    if (task.getAssignee() != null && task.getAssignee().getId() != null) {
        User assignee = userRepository.findById(task.getAssignee().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setAssignee(assignee);
    }

    Task saved = taskRepository.save(task);
    return toDTO(saved);
}

    public TaskDTO updateTask(Long id, Task updatedTask) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (updatedTask.getName() != null) {
            existing.setName(updatedTask.getName());
        }
        if (updatedTask.getDescription() != null) {
            existing.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getIndex() != null) {
            existing.setIndex(updatedTask.getIndex());
        }
        if (updatedTask.getTaskStatus() != null && updatedTask.getTaskStatus().getId() != null) {
            TaskStatus status = taskStatusRepository.findById(updatedTask.getTaskStatus().getId())
                    .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
            existing.setTaskStatus(status);
        }
        if (updatedTask.getAssignee() != null && updatedTask.getAssignee().getId() != null) {
            User assignee = userRepository.findById(updatedTask.getAssignee().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existing.setAssignee(assignee);
        }

        Task saved = taskRepository.save(existing);
        return toDTO(saved);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    private TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setIndex(task.getIndex());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
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
