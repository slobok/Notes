package com.example.Notes.views.List;

import com.example.Notes.Data.Note;
import com.example.Notes.Services.NoteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;


@Route(value = "",layout = MainLayout.class)
public class NotesList extends VerticalLayout {
    private NoteService noteService;

    private VerticalLayout noteslist = new VerticalLayout();

    NotesList(NoteService noteService){
        this.noteService = noteService;
        this.noteslist = getAllNotes();
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
            this.noteService.saveNote(new Note(notesTitle.getValue(), textArea.getValue(),1L));
              notesTitle.setValue("");
              textArea.setValue("");
              this.updatePage();
        });
        newNote.add(notesTitle, textArea, createNote);
        return newNote;
    }
    // Metoda koja dovlaci iz baze sve notese
    private VerticalLayout getAllNotes(){
        VerticalLayout notesList = new VerticalLayout();
        notesList.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.noteService.getAllNotes().forEach(n -> {

            VerticalLayout note = new VerticalLayout();
            note.getStyle().setDisplay(Style.Display.INLINE_BLOCK);


            note.setWidth("30%");
            note.setMargin(true);
            note.getStyle().setBorder("1px solid black");

            TextField notesTitle = new TextField();
            notesTitle.setValue(n.getTitle());
            notesTitle.setLabel("Title");
            notesTitle.getStyle().setMargin("0");

            TextField notesText = new TextField();
            notesText.setValue(n.getText());
            notesText.setLabel("Notes text");
            notesText.getStyle().setMargin("0");

            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(click -> {
                this.noteService.deleteNote(n);
                this.updatePage();
            });

            note.add(notesTitle, notesText, deleteButton);

            notesList.add(note);
                }
        );
     return notesList;
    }

    private void updatePage(){
        this.removeAll();
        this.add(
                this.createNoteField(),
                this.getAllNotes()
        );
    }
}
