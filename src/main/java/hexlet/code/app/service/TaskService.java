package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasks(String titleCont, Long assigneeId, String status, Long labelId);

    TaskDTO getTaskById(Long id);

    TaskDTO createTask(TaskCreateDTO dto);

    TaskDTO updateTask(Long id, TaskCreateDTO dto);

    void deleteTask(Long id);
}
