package hexlet.code.app.service;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.LabelDTO;

import java.util.List;

public interface LabelService {

    List<LabelDTO> getAllLabels();

    LabelDTO getLabelById(Long id);

    LabelDTO createLabel(LabelCreateDTO dto);

    LabelDTO updateLabel(Long id, LabelCreateDTO dto);

    void deleteLabel(Long id);
}
