package com.example.Notes.Services;

import com.example.Notes.Data.Label;
import com.example.Notes.Repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository){
         this.labelRepository = labelRepository;
    }

    public List<Label> getAllLabels(){
        return this.labelRepository.findAll();
    }
    
    public void addLabel(String labelName){
        if(labelName.isEmpty() || labelName == ""){
            throw new IllegalArgumentException("Label must contain name!!!");
        }

        this.labelRepository.save(new Label(labelName));
    }
}
