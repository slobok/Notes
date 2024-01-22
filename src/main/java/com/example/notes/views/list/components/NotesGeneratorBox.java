package com.example.notes.views.list.components;


import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

// Zasad se komponenta ne koristi
public class NotesGeneratorBox extends HorizontalLayout {
    private final NoteService noteService;
    Input input;
    public NotesGeneratorBox(NoteService noteService){
        this.noteService = noteService;

        input = new Input();
        input.setType("number");
        input.setValue("1000");
        Button button = new Button("Generate notes");
        stylizeThisComponent();
        this.add(input, button);
        button.addClickListener(event -> {
            this.noteService.addManyNotesToDatabase(Integer.parseInt(input.getValue()));
        });
    }

    private void stylizeThisComponent() {
        this.getStyle().setPosition(Style.Position.ABSOLUTE);
        this.getStyle().setTop("70px");
        this.getStyle().setRight("13px");
        this.getStyle().setBackground("linen");
        this.getStyle().setBorder("2px solid black");
    }


}
