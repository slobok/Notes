package com.example.Notes.views.List;

import com.example.Notes.Data.Note;
import com.example.Notes.Services.NoteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;


@Route(value = "",layout = MainLayout.class)
public class NotesList extends VerticalLayout {
    protected NoteService noteService;
    private TextField search = new TextField();

    public TextField getSearch() {
        return search;
    }

    NotesList(NoteService noteService){
        this.noteService = noteService;
        addToConstructor();
    }

    protected void addToConstructor(){
        this.add(
                getSearchField(),
                createNoteField(),
                getAllNotes()
        );
    }

    protected Component getSearchField() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidthFull();
        this.search.setPlaceholder("Type to search");
        search.addValueChangeListener(e -> {
            this.updatePage();
        });
        hl.add(search);
        return  hl;
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
    protected VerticalLayout getAllNotes(){
        VerticalLayout notesList = new VerticalLayout();
        notesList.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.noteService.getAllNotes(this.search.getValue()).forEach(n -> {

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


            Button deleteButton = new Button("Move to trash");
            deleteButton.addClickListener(click -> {
                this.noteService.moveToTrash(n);
                this.updatePage();
            });
            Button archiveButton = new Button("Archive");
            archiveButton.addClickListener(click -> {
                this.noteService.updateStateIsArchived(n.getId());
                this.updatePage();
            });

            Button updateChanges = new Button("Save");
            updateChanges.addClickListener(click -> {
                Note noteToUpdate = new Note();
                noteToUpdate.setId(n.getId());
                noteToUpdate.setTitle(notesTitle.getValue());
                noteToUpdate.setText(notesText.getValue());
                noteToUpdate.setCreatedByUser(n.getCreatedByUser());
                
                this.noteService.saveNote(noteToUpdate);
                this.updatePage();
            });

            note.add(notesTitle, notesText, deleteButton, updateChanges, archiveButton);

            notesList.add(note);
                }
        );
     return notesList;
    }

    protected void updatePage(){
        this.removeAll();
        this.add(
                this.getSearchField(),
                this.createNoteField(),
                this.getAllNotes()
        );
    }
}
