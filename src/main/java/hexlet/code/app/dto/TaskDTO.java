package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Integer index;
    private String name;
    private String title;          // для тестов
    private String description;
    private String content;        // для тестов
    private String status;         // ← ДОБАВЛЕНО
    private Long taskStatusId;
    private Long assigneeId;
    private List<Long> labelIds;
    private LocalDate createdAt;
}
