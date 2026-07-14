package hexlet.code.app.service;

import hexlet.code.app.dto.LabelDTO;
import hexlet.code.app.model.Label;

import java.util.List;

public interface LabelService {

    List<LabelDTO> getAllLabels();

    LabelDTO getLabelById(Long id);

    LabelDTO createLabel(Label label);

    LabelDTO updateLabel(Long id, Label updatedLabel);

    void deleteLabel(Long id);
}
