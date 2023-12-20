package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class CreatingNewNoteComponent extends VerticalLayout {
    private TextField newNoteTitle;
    private TextArea  newNoteText;
    private HorizontalLayout noteMenu;
    private Button createNoteButton;

    private final NoteService noteService;

    public CreatingNewNoteComponent(NoteService noteService){
        this.noteService = noteService;
        this.styleThisComponent();

        this.newNoteTitle = getNotesTitle();
        this.newNoteText = getNoteText();
        this.noteMenu = getNoteMenu();
        this.createNoteButton = getCreateNewNoteButton();
        add(newNoteTitle, newNoteText, createNoteButton);
    }

    public TextField getNewNoteTitle() {
        return newNoteTitle;
    }

    public TextArea getNewNoteText() {
        return newNoteText;
    }

    private void styleThisComponent(){
        this.setAlignItems(Alignment.CENTER);
        this.getStyle().setBoxShadow("1px 1px 2px 2px linen");
    }

    private TextField getNotesTitle(){
        TextField notesTitle = new TextField();
        notesTitle.setPlaceholder("Title");
        notesTitle.setVisible(false);
        notesTitle.setWidth("30%");
        return  notesTitle;
    }

    private TextArea getNoteText(){
        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Type here");
        textArea.setWidth("30%");
        textArea.addFocusListener(e -> {
            newNoteTitle.setVisible(true);
        });
        return  textArea;
    }

    private HorizontalLayout getNoteMenu(){
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.setAlignItems(Alignment.START);
        noteMenu.getStyle().setBorder("1px solid black");
        Icon imageIcon = new Icon("lumo", "photo");
        noteMenu.add(imageIcon);
        return noteMenu;
    }

    private Button getCreateNewNoteButton(){
        Button createNoteButton = new Button("Create note");
        createNoteButton.addClickListener(click -> {
            createNewNote(getNotesTitle(), getNewNoteText());
        });
        return  createNoteButton;
    }

    private void createNewNote(TextField newNoteTitle, TextArea newNoteText) {
        this.noteService.saveNote(new Note(newNoteTitle.getValue(),  newNoteText.getValue(),1L));
        this.newNoteText.setValue("");
        this.newNoteTitle.setValue("");
    }
}
