package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapperService taskMapperService;

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
                .map(taskMapperService::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapperService.toDTO(task);
    }

    @Override
    public TaskDTO createTask(TaskCreateDTO dto) {
        Task task = taskMapperService.toEntity(dto);
        Task saved = taskRepository.save(task);
        return taskMapperService.toDTO(saved);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskCreateDTO dto) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskMapperService.updateEntity(dto, existing);
        Task saved = taskRepository.save(existing);
        return taskMapperService.toDTO(saved);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
