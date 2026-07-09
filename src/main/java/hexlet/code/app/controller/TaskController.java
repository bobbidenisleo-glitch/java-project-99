package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskStatusRepository taskStatusRepository;

    @GetMapping
public ResponseEntity<List<TaskDTO>> getAllTasks(
        @RequestParam(required = false) String titleCont,
        @RequestParam(required = false) Long assigneeId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long labelId
) {
    List<TaskDTO> tasks = taskService.getAllTasks(titleCont, assigneeId, status, labelId);
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Total-Count", String.valueOf(tasks.size()));
    return ResponseEntity.ok()
            .headers(headers)
            .body(tasks);
}

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody Map<String, Object> taskData) {
        TaskCreateDTO dto = new TaskCreateDTO();
        
        // Преобразуем поля из фронтенда
        dto.setName((String) taskData.getOrDefault("title", taskData.get("name")));
        dto.setDescription((String) taskData.getOrDefault("content", taskData.get("description")));
        dto.setIndex(taskData.get("index") != null ? ((Number) taskData.get("index")).intValue() : null);
        
        // Преобразуем статус (slug → id)
        if (taskData.containsKey("status")) {
            String statusSlug = (String) taskData.get("status");
            TaskStatus status = taskStatusRepository.findBySlug(statusSlug)
                    .orElseThrow(() -> new RuntimeException("Status not found: " + statusSlug));
            dto.setTaskStatusId(status.getId());
        } else if (taskData.containsKey("taskStatusId")) {
            dto.setTaskStatusId(((Number) taskData.get("taskStatusId")).longValue());
        }
        
        // Преобразуем исполнителя
        if (taskData.containsKey("assignee_id")) {
            dto.setAssigneeId(((Number) taskData.get("assignee_id")).longValue());
        } else if (taskData.containsKey("assigneeId")) {
            dto.setAssigneeId(((Number) taskData.get("assigneeId")).longValue());
        }
        
        // Преобразуем метки
        if (taskData.containsKey("taskLabelIds")) {
            List<Long> labelIds = (List<Long>) taskData.get("taskLabelIds");
            dto.setLabelIds(labelIds);
        } else if (taskData.containsKey("labelIds")) {
            List<Long> labelIds = (List<Long>) taskData.get("labelIds");
            dto.setLabelIds(labelIds);
        }
        
        TaskDTO created = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskCreateDTO taskCreateDTO) {
        TaskDTO updated = taskService.updateTask(id, taskCreateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
