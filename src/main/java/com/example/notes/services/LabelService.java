package com.example.notes.services;

import com.example.notes.data.Label;
import com.example.notes.repository.LabelRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public void saveLabel(Label label){
        this.labelRepository.save(label);
    }

    public int countLabels(){
        return (int)labelRepository.count();
    }

    @Transactional
    public void editLabelName(Long labelId, String name){
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new IllegalArgumentException("Note found note with id" + labelId));
        label.setName(name);
    }

    public List<Label> getAllLabels() {
        return this.labelRepository.findAll();
    }

    public void addLabel(String labelName) {
        if (labelName == null || labelName.isEmpty()) {
            throw new IllegalArgumentException("Label must contain name!!!");
        }
        if(!this.labelRepository.findByName(labelName).isEmpty()){
          throw  new IllegalArgumentException("Label with " + labelName + "already exist");
        }
        this.labelRepository.save(new Label(labelName));
    }

    public void deleteLabel(Label label){
        this.labelRepository.delete(label);;
    }

}