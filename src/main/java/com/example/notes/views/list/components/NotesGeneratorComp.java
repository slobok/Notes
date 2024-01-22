package com.example.notes.views.list.components;

import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.value.ValueChangeMode;


public class NotesGeneratorComp extends Div {
    NumberField numberField;
    Button addNotesButton;
    private final NoteService noteService;
     public  NotesGeneratorComp(NoteService noteService){
         this.noteService = noteService;
         setNumberField();
         setAddNotesButton();
         add(numberField, addNotesButton);

    }

    public void setNumberField(){
        numberField = new NumberField("Number of notes to generate");
        numberField.setValueChangeMode(ValueChangeMode.LAZY);
        numberField.addValueChangeListener(event -> {
            addNotesButton.setEnabled(!numberField.getValue().equals(0D) && numberField.getValue() != null);
        });
    }

    public void setAddNotesButton() {
         addNotesButton = new Button("Generate notes");
         addNotesButton.setEnabled(false);
            addNotesButton.addClickListener(event -> {
                noteService.addManyNotesToDatabase(numberField.getValue().intValue());
        });
    }
}
