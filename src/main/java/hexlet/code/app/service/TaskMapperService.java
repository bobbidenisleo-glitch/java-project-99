package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskMapperService {

    private final TaskMapper taskMapper;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public Task toEntity(TaskCreateDTO dto) {
        Task task = taskMapper.toEntity(dto);
        
        // Ручное заполнение name из title (для тестов Hexlet)
        if (dto.getName() == null || dto.getName().isEmpty()) {
            task.setName(dto.getTitle());
        }
        
        // Ручное заполнение description из content (для тестов Hexlet)
        if (dto.getDescription() == null || dto.getDescription().isEmpty()) {
            task.setDescription(dto.getContent());
        }

        // Сложная логика: установка статуса
        if (dto.getTaskStatusId() != null) {
            TaskStatus status = taskStatusRepository.findById(dto.getTaskStatusId())
                    .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
            task.setTaskStatus(status);
        }
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            TaskStatus status = taskStatusRepository.findBySlug(dto.getStatus())
                    .orElseThrow(() -> new RuntimeException("Status not found: " + dto.getStatus()));
            task.setTaskStatus(status);
        }
        if (dto.getTaskStatus() != null) {
            task.setTaskStatus(dto.getTaskStatus());
        }

        // Сложная логика: установка исполнителя
        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignee(assignee);
        }
        if (dto.getAssignee() != null) {
            task.setAssignee(dto.getAssignee());
        }

        // Сложная логика: установка меток
        List<Long> labelIds = dto.getLabelIds();
        if ((labelIds == null || labelIds.isEmpty()) && dto.getTaskLabelIds() != null && !dto.getTaskLabelIds().isEmpty()) {
            labelIds = dto.getTaskLabelIds();
        }
        if ((labelIds == null || labelIds.isEmpty()) && dto.getLabels() != null && !dto.getLabels().isEmpty()) {
            labelIds = dto.getLabels();
        }
        if (labelIds != null && !labelIds.isEmpty()) {
            Set<Label> labels = new HashSet<>(labelRepository.findAllById(labelIds));
            task.setLabels(labels);
        }

        return task;
    }

    public TaskDTO toDTO(Task task) {
        TaskDTO dto = taskMapper.toDTO(task);
        
        dto.setTitle(task.getName());
        dto.setContent(task.getDescription());
        
        if (task.getTaskStatus() != null) {
            dto.setStatus(task.getTaskStatus().getSlug());
            dto.setTaskStatusId(task.getTaskStatus().getId());
        }
        
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }
        
        if (task.getLabels() != null && !task.getLabels().isEmpty()) {
            List<Long> labelIds = task.getLabels().stream()
                    .map(Label::getId)
                    .collect(Collectors.toList());
            dto.setLabelIds(labelIds);
            dto.setTaskLabelIds(labelIds);
        } else {
            dto.setLabelIds(new ArrayList<>());
            dto.setTaskLabelIds(new ArrayList<>());
        }
        
        return dto;
    }

    public void updateEntity(TaskCreateDTO dto, Task task) {
        // Обновление name / title
        if (dto.getTitle() != null) {
            task.setName(dto.getTitle());
        }
        if (dto.getName() != null) {
            task.setName(dto.getName());
        }
        
        // Обновление description / content
        if (dto.getContent() != null) {
            task.setDescription(dto.getContent());
        }
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }
        
        // Индекс
        if (dto.getIndex() != null) {
            task.setIndex(dto.getIndex());
        }

        // Обновление статуса
        if (dto.getTaskStatusId() != null) {
            TaskStatus status = taskStatusRepository.findById(dto.getTaskStatusId())
                    .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
            task.setTaskStatus(status);
        }
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            TaskStatus status = taskStatusRepository.findBySlug(dto.getStatus())
                    .orElseThrow(() -> new RuntimeException("Status not found: " + dto.getStatus()));
            task.setTaskStatus(status);
        }
        if (dto.getTaskStatus() != null) {
            task.setTaskStatus(dto.getTaskStatus());
        }

        // Обновление исполнителя
        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignee(assignee);
        }
        if (dto.getAssignee() != null) {
            task.setAssignee(dto.getAssignee());
        }

        // Обновление меток
        List<Long> labelIds = dto.getLabelIds();
        if ((labelIds == null || labelIds.isEmpty()) && dto.getTaskLabelIds() != null && !dto.getTaskLabelIds().isEmpty()) {
            labelIds = dto.getTaskLabelIds();
        }
        if ((labelIds == null || labelIds.isEmpty()) && dto.getLabels() != null && !dto.getLabels().isEmpty()) {
            labelIds = dto.getLabels();
        }
        if (labelIds != null && !labelIds.isEmpty()) {
            Set<Label> labels = new HashSet<>(labelRepository.findAllById(labelIds));
            task.setLabels(labels);
        }
    }
}
