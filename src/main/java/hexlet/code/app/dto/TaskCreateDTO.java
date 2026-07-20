package hexlet.code.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import lombok.Data;

import java.util.List;

@Data
public class TaskCreateDTO {
    private String name;
    private String description;
    private Integer index;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private Long taskStatusId;
    private List<Long> labelIds;
    private String title;
    private String content;
    private String status;
    private List<Long> taskLabelIds;
    private TaskStatus taskStatus;
    private User assignee;
    private List<Long> labels;
}
