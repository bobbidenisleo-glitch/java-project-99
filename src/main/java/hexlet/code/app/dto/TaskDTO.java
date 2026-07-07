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
    private String description;
    private Long taskStatusId;
    private Long assigneeId;
    private List<Long> labelIds;  // ← ДОБАВИТЬ
    private LocalDate createdAt;
}
