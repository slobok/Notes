package com.example.notes.views.list.components;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.example.notes.views.list.events.NoteCreatedEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class NewNoteForm extends VerticalLayout {

    private final NoteService noteService;
    public NewNoteForm(NoteService noteService){
        this.noteService = noteService;
        this.setAlignItems(FlexComponent.Alignment.CENTER);
        this.getStyle().setBoxShadow("1px 1px 2px linen");

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

        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.setAlignItems(FlexComponent.Alignment.START);
        noteMenu.getStyle().setBorder("1px solid black");
        Icon imageIcon = new Icon("lumo", "photo");
        noteMenu.add(imageIcon);

        Button createNote = new Button("Create note");
        createNote.setEnabled(false);
        createNote.addClickListener(click -> {
            createNewNote(notesTitle, textArea);
            //Daj signal da je doslo do promjene u brojevima
            ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(this,false));
        });
        notesTitle.addValueChangeListener(event -> {
            createNote.setEnabled(!event.getValue().isBlank() || !textArea.getValue().isBlank());
        });
        textArea.addValueChangeListener(event -> {
            createNote.setEnabled(!event.getValue().isBlank() || !notesTitle.getValue().isBlank());
        });

        this.add(notesTitle, textArea, createNote);
    }

    private void createNewNote(TextField notesTitle, TextArea textArea) {
        this.noteService.saveNote(new Note(notesTitle.getValue(), textArea.getValue(), 1L));
        notesTitle.setValue("");
        textArea.setValue("");
        ComponentUtil.fireEvent(UI.getCurrent(), new NoteCreatedEvent(this, false));
    }
}
