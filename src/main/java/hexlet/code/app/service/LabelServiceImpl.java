package hexlet.code.app.service;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.LabelDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public List<LabelDTO> getAllLabels() {
        return labelRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LabelDTO getLabelById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Label not found"));
        return toDTO(label);
    }

    @Override
    public LabelDTO createLabel(LabelCreateDTO dto) {
        Label label = new Label();
        label.setName(dto.getName());

        Label saved = labelRepository.save(label);
        return toDTO(saved);
    }

    @Override
    public LabelDTO updateLabel(Long id, LabelCreateDTO dto) {
        Label existing = labelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Label not found"));

        if (dto.getName() != null) {
            existing.setName(dto.getName());
        }

        Label saved = labelRepository.save(existing);
        return toDTO(saved);
    }

    @Override
    public void deleteLabel(Long id) {
        if (!labelRepository.existsById(id)) {
            throw new RuntimeException("Label not found");
        }
        labelRepository.deleteById(id);
    }

    private LabelDTO toDTO(Label label) {
        LabelDTO dto = new LabelDTO();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setCreatedAt(label.getCreatedAt());
        return dto;
    }
}
