package com.example.Notes.views.List;

import com.example.Notes.Data.Note;
import com.example.Notes.Services.NoteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;


@Route(value = "",layout = MainLayout.class)
public class NotesList extends VerticalLayout {
    private NoteService noteService;
    NotesList(NoteService noteService){
        this.noteService = noteService;

        add(
                createNoteField(),
                getAllNotes()
        );
    }

    private Component createNoteField(){
        VerticalLayout newNote = new VerticalLayout();
        newNote.setAlignItems(Alignment.CENTER);
        newNote.getStyle().setBorder("1px solid black");

        TextField notesTitle = new TextField();
        notesTitle.setPlaceholder("Title");
        notesTitle.setVisible(false);
        notesTitle.setWidth("30%");

        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Type here");
        textArea.setWidth("30%");
        textArea.addFocusListener(e -> {
            notesTitle.setVisible(true);
        });

        Button createNote = new Button("Create note");
        createNote.addClickListener(click -> {
            this.noteService.saveNote(new Note(notesTitle.getValue(),textArea.getValue(),1L));
            this.add(this.getAllNotes());
        });
        newNote.add(notesTitle, textArea, createNote);
        return newNote;
    }
    private Component getAllNotes(){
        VerticalLayout notesList = new VerticalLayout();
        this.noteService.getAllNotes().forEach(n -> {
            TextField notesTitle = new TextField();
            notesTitle.setValue(n.getTitle());
            TextField notesText = new TextField();
            notesText.setValue(n.getText());

            notesList.add(notesText);
                }
        );
     return notesList;
    }
}
