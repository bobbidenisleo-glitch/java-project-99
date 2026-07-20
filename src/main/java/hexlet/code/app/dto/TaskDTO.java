package hexlet.code.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDTO {
    private Long id;
    private Integer index;
    private String name;
    private String title;
    private String description;
    private String content;
    private String status;
    private Long taskStatusId;
    
    @JsonProperty("assignee_id")
    private Long assigneeId;
    
    private List<Long> labelIds;
    private List<Long> taskLabelIds;
    private LocalDate createdAt;
}
