package com.example.notes.services;

import com.example.notes.data.Label;
import com.example.notes.data.Note;
import com.example.notes.repository.LabelRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<Note> getLabelNotes(Long labelId){
        Label label =  this.labelRepository.findById(labelId)
                .orElseThrow(() -> new IllegalArgumentException("Note found label with id"));
        System.out.println(label.getName());
        System.out.println(new ArrayList<>(label.getLabeledNotes()));
        return  new ArrayList<>(label.getLabeledNotes());
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
        Optional <Label> optionalLabel = labelRepository.findByName(labelName);
        if(optionalLabel.isPresent()){
            throw new IllegalArgumentException("Already exists label with name" + labelName);
        }
        this.labelRepository.save(new Label(labelName));
    }

    public void deleteLabel(Label label){
        this.labelRepository.delete(label);;
    }

    public Label findLabelByName(String labelName){
        return this.labelRepository.findByName(labelName)
                .orElseThrow(() -> new IllegalArgumentException("Not found label wiht name" + labelName   ));
    }

    @Transactional
    public List<Note> getLabelNotes(Label label){
        return new ArrayList<>(label.getLabeledNotes());
    }
}