package com.example.notes.views.list.components;

import com.example.notes.services.NoteService;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.NumberField;

public class NotesGeneratorComp extends Div {
    NumberField numberField;
    Button addNotesButton;
    Button deleteButton;
    private final NoteService noteService;
    public  NotesGeneratorComp(NoteService noteService){
        this.noteService = noteService;
        setNumberField();
        setAddNotesButton();
        setDeleteButton();
        add(numberField, addNotesButton ,deleteButton);
    }

    public void setNumberField(){
        numberField = new NumberField("Number of notes to generate");
    }

    public void setAddNotesButton() {
        addNotesButton = new Button("Add notes");
        addNotesButton.addClickListener(event -> {
            noteService.addManyNotesToDatabase(numberField.getValue().intValue());
            ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(this, false));
        });
    }

    public void setDeleteButton(){
        deleteButton = new Button("Delete all");
        deleteButton.addClickListener(event -> {
            this.noteService.deleteAllNotes();
            ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(this, false));
        });
    }
}