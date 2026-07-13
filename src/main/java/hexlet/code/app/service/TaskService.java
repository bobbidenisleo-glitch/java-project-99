package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public List<TaskDTO> getAllTasks(String titleCont, Long assigneeId, String status, Long labelId) {
        Specification<Task> spec = (root, query, cb) -> cb.conjunction();
        
        if (titleCont != null && !titleCont.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("name")), "%" + titleCont.toLowerCase() + "%")
            );
        }
        
        if (assigneeId != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("assignee").get("id"), assigneeId)
            );
        }
        
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("taskStatus").get("slug"), status)
            );
        }
        
        if (labelId != null) {
            spec = spec.and((root, query, cb) -> {
                var labelsJoin = root.join("labels");
                return cb.equal(labelsJoin.get("id"), labelId);
            });
        }
        
        List<Task> tasks = taskRepository.findAll(spec);
        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.toDTO(task);
    }

    public TaskDTO createTask(TaskCreateDTO dto) {
        Task task = taskMapper.toEntity(dto);
        Task saved = taskRepository.save(task);
        return taskMapper.toDTO(saved);
    }

    public TaskDTO updateTask(Long id, TaskCreateDTO dto) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (dto.getName() != null) {
            existing.setName(dto.getName());
        }
        if (dto.getTitle() != null) {
            existing.setName(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            existing.setDescription(dto.getDescription());
        }
        if (dto.getContent() != null) {
            existing.setDescription(dto.getContent());
        }
        if (dto.getIndex() != null) {
            existing.setIndex(dto.getIndex());
        }
        if (dto.getTaskStatusId() != null) {
            TaskStatus status = taskStatusRepository.findById(dto.getTaskStatusId())
                    .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
            existing.setTaskStatus(status);
        }
        if (dto.getStatus() != null) {
            TaskStatus status = taskStatusRepository.findBySlug(dto.getStatus())
                    .orElseThrow(() -> new RuntimeException("Status not found: " + dto.getStatus()));
            existing.setTaskStatus(status);
        }
        if (dto.getTaskStatus() != null) {
            existing.setTaskStatus(dto.getTaskStatus());
        }
        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existing.setAssignee(assignee);
        }
        if (dto.getAssignee() != null) {
            existing.setAssignee(dto.getAssignee());
        }
        if (dto.getLabelIds() != null && !dto.getLabelIds().isEmpty()) {
            // TODO: обновить метки
        }

        Task saved = taskRepository.save(existing);
        return taskMapper.toDTO(saved);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
