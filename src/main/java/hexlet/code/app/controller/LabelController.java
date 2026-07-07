package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.service.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @GetMapping
    public ResponseEntity<List<LabelDTO>> getAllLabels() {
        List<LabelDTO> labels = labelService.getAllLabels();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(labels.size()));
        return ResponseEntity.ok()
                .headers(headers)
                .body(labels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelDTO> getLabelById(@PathVariable Long id) {
        LabelDTO label = labelService.getLabelById(id);
        return ResponseEntity.ok(label);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LabelDTO> createLabel(@Valid @RequestBody Label label) {
        LabelDTO created = labelService.createLabel(label);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelDTO> updateLabel(@PathVariable Long id, @RequestBody Label label) {
        LabelDTO updated = labelService.updateLabel(id, label);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
}
