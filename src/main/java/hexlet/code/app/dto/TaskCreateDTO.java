package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {
    private String name;
    private String description;
    private Integer index;
    private Long taskStatusId;
    private Long assigneeId;
}
