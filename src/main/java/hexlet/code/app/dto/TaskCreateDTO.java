package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {
    @NotBlank
    @Size(min = 1)
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
}
