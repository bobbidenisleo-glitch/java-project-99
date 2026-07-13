package hexlet.code.app.dto;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {
    // Основные поля
    private String name;
    private String description;
    private Integer index;

    private Long taskStatusId;
    private Long assigneeId;
    private List<Long> labelIds;

    // Поля для совместимости с тестами
    private String title;
    private String content;
    private String status;
    private List<Long> taskLabelIds;

    // Поля для совместимости с Entity
    private TaskStatus taskStatus;
    private User assignee;

    // Дополнительное поле для совместимости
    private List<Long> labels;
}
