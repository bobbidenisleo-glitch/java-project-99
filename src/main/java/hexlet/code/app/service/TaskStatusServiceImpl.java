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
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public List<TaskStatusDTO> getAllStatuses() {
        return taskStatusRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskStatusDTO getStatusById(Long id) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
        return toDTO(status);
    }

    @Override
    public TaskStatusDTO createStatus(TaskStatus status) {
        if (taskStatusRepository.findBySlug(status.getSlug()).isPresent()) {
            throw new RuntimeException("Status with this slug already exists");
        }
        TaskStatus saved = taskStatusRepository.save(status);
        return toDTO(saved);
    }

    @Override
    public TaskStatusDTO updateStatus(Long id, TaskStatus updatedStatus) {
        TaskStatus existing = taskStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskStatus not found"));

        if (updatedStatus.getName() != null) {
            existing.setName(updatedStatus.getName());
        }
        if (updatedStatus.getSlug() != null) {
            if (taskStatusRepository.findBySlug(updatedStatus.getSlug()).isPresent() &&
                !existing.getSlug().equals(updatedStatus.getSlug())) {
                throw new RuntimeException("Status with this slug already exists");
            }
            existing.setSlug(updatedStatus.getSlug());
        }

        TaskStatus saved = taskStatusRepository.save(existing);
        return toDTO(saved);
    }

    @Override
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
