package hexlet.code.app.specification;

import hexlet.code.app.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    public Specification<Task> build(String titleCont, Long assigneeId, String status, Long labelId) {
        Specification<Task> spec = (root, query, cb) -> cb.conjunction();

        if (titleCont != null && !titleCont.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + titleCont.toLowerCase() + "%")
            );
        }

        if (assigneeId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("assignee").get("id"), assigneeId)
            );
        }

        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("taskStatus").get("slug"), status)
            );
        }

        if (labelId != null) {
            spec = spec.and((root, query, cb) -> {
                var labelsJoin = root.join("labels");
                return cb.equal(labelsJoin.get("id"), labelId);
            });
        }

        return spec;
    }
}
