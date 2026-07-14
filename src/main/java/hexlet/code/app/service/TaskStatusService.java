package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusDTO;
import hexlet.code.app.model.TaskStatus;

import java.util.List;

public interface TaskStatusService {

    List<TaskStatusDTO> getAllStatuses();

    TaskStatusDTO getStatusById(Long id);

    TaskStatusDTO createStatus(TaskStatus status);

    TaskStatusDTO updateStatus(Long id, TaskStatus updatedStatus);

    void deleteStatus(Long id);
}
