package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    @Override
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

        return taskRepository.findAll(spec).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return toDTO(task);
    }

    @Override
    public TaskDTO createTask(TaskCreateDTO dto) {
        Task task = new Task();
        
        // Обработка name / title
        String name = dto.getName();
        if (name == null || name.isEmpty()) {
            name = dto.getTitle();
        }
        task.setName(name);
        
        // Обработка description / content
        String description = dto.getDescription();
        if (description == null || description.isEmpty()) {
            description = dto.getContent();
        }
        task.setDescription(description);
        
        task.setIndex(dto.getIndex());
        
        // Установка статуса по ID
        if (dto.getTaskStatusId() != null) {
            TaskStatus status = taskStatusRepository.findById(dto.getTaskStatusId())
                    .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
            task.setTaskStatus(status);
        }
        
        // Установка статуса по слагу (для тестов Hexlet)
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            TaskStatus status = taskStatusRepository.findBySlug(dto.getStatus())
                    .orElseThrow(() -> new RuntimeException("Status not found: " + dto.getStatus()));
            task.setTaskStatus(status);
        }
        
        // Установка исполнителя по ID
        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignee(assignee);
        }
        
        // Установка меток
        if (dto.getLabelIds() != null && !dto.getLabelIds().isEmpty()) {
            List<Label> labels = labelRepository.findAllById(dto.getLabelIds());
            task.setLabels(labels);
        }
        
        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    @Override
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
            List<Label> labels = labelRepository.findAllById(dto.getLabelIds());
            existing.setLabels(labels);
        }
        
        Task saved = taskRepository.save(existing);
        return toDTO(saved);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    private TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setTitle(task.getName());
        dto.setDescription(task.getDescription());
        dto.setContent(task.getDescription());
        dto.setIndex(task.getIndex());
        dto.setCreatedAt(task.getCreatedAt());
        
        if (task.getTaskStatus() != null) {
            dto.setTaskStatusId(task.getTaskStatus().getId());
            dto.setStatus(task.getTaskStatus().getSlug());
        }
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }
        if (task.getLabels() != null) {
            dto.setLabelIds(task.getLabels().stream()
                    .map(Label::getId)
                    .collect(Collectors.toList()));
            dto.setTaskLabelIds(dto.getLabelIds());
        }
        return dto;
    }
}
