package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;
    private final TaskMapperService taskMapperService;

    @Override
    public List<TaskDTO> getAllTasks(String titleCont, Long assigneeId, String status, Long labelId) {
        Specification<Task> spec = taskSpecification.build(titleCont, assigneeId, status, labelId);
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
        Task task = taskMapper.toEntity(dto);
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
