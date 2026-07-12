package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusDTO;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.service.TaskStatusService;
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

@RestController
@RequestMapping("/api/task_statuses")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    @GetMapping
    public ResponseEntity<List<TaskStatusDTO>> getAllStatuses() {
        List<TaskStatusDTO> statuses = taskStatusService.getAllStatuses();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(statuses.size()));
        return ResponseEntity.ok()
                .headers(headers)
                .body(statuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusDTO> getStatusById(@PathVariable Long id) {
        TaskStatusDTO status = taskStatusService.getStatusById(id);
        return ResponseEntity.ok(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskStatusDTO> createStatus(@Valid @RequestBody TaskStatusCreateDTO statusCreateDTO) {
        TaskStatus status = new TaskStatus();
        status.setName(statusCreateDTO.getName());
        status.setSlug(statusCreateDTO.getSlug());
        
        TaskStatusDTO created = taskStatusService.createStatus(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusDTO> updateStatus(@PathVariable Long id, @RequestBody TaskStatusCreateDTO statusCreateDTO) {
        TaskStatus status = new TaskStatus();
        status.setName(statusCreateDTO.getName());
        status.setSlug(statusCreateDTO.getSlug());
        
        TaskStatusDTO updated = taskStatusService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        taskStatusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}
