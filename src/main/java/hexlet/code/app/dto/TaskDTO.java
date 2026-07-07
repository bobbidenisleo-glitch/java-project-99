package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Integer index;
    private String name;
    private String description;
    private Long taskStatusId;
    private Long assigneeId;
    private LocalDate createdAt;
}
