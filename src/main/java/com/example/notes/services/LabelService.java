package com.example.notes.services;

import com.example.notes.data.Label;
import com.example.notes.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<Label> getAllLabels() {
        return this.labelRepository.findAll();
    }

    public void addLabel(String labelName) {
        if (labelName == null || labelName.isEmpty()) {
            throw new IllegalArgumentException("Label must contain name!!!");
        }

        this.labelRepository.save(new Label(labelName));
    }
}
