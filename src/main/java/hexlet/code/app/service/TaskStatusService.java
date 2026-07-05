package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusDTO;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    public List<TaskStatusDTO> getAllStatuses() {
        return taskStatusRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TaskStatusDTO getStatusById(Long id) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
        return toDTO(status);
    }

    public TaskStatusDTO createStatus(TaskStatus status) {
        // Проверяем уникальность slug
        if (taskStatusRepository.findBySlug(status.getSlug()).isPresent()) {
            throw new RuntimeException("Slug already exists");
        }
        TaskStatus saved = taskStatusRepository.save(status);
        return toDTO(saved);
    }

    public TaskStatusDTO updateStatus(Long id, TaskStatus updatedStatus) {
        TaskStatus existing = taskStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskStatus not found"));

        if (updatedStatus.getName() != null) {
            existing.setName(updatedStatus.getName());
        }
        if (updatedStatus.getSlug() != null) {
            // Проверяем уникальность нового slug
            if (taskStatusRepository.findBySlug(updatedStatus.getSlug()).isPresent() &&
                !existing.getSlug().equals(updatedStatus.getSlug())) {
                throw new RuntimeException("Slug already exists");
            }
            existing.setSlug(updatedStatus.getSlug());
        }

        TaskStatus saved = taskStatusRepository.save(existing);
        return toDTO(saved);
    }

    public void deleteStatus(Long id) {
        if (!taskStatusRepository.existsById(id)) {
            throw new RuntimeException("TaskStatus not found");
        }
        taskStatusRepository.deleteById(id);
    }

    private TaskStatusDTO toDTO(TaskStatus status) {
        TaskStatusDTO dto = new TaskStatusDTO();
        dto.setId(status.getId());
        dto.setName(status.getName());
        dto.setSlug(status.getSlug());
        dto.setCreatedAt(status.getCreatedAt());
        return dto;
    }
}
