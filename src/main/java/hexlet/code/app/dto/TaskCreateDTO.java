package hexlet.code.app.dto;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import lombok.Data;

import java.util.List;

@Data
public class TaskCreateDTO {
    private String name;
    private String description;
    private Integer index;
    private Long taskStatusId;
    private Long assigneeId;
    private List<Long> labelIds;
    private String title;
    private String content;
    private String status;
    private List<Long> taskLabelIds;
    private TaskStatus taskStatus;
    private User assignee;
    private List<Long> labels;
}
