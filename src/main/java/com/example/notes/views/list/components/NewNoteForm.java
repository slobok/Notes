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
import com.vaadin.flow.data.value.ValueChangeMode;

public class NewNoteForm extends VerticalLayout {

    private final NoteService noteService;
    public NewNoteForm(NoteService noteService){
        this.noteService = noteService;
        this.setAlignItems(FlexComponent.Alignment.CENTER);
        this.getStyle().setBoxShadow("1px 1px 2px linen");

        TextField notesTitle = new TextField();

        notesTitle.setPlaceholder("Note title");
        notesTitle.setVisible(false);
        //TODO podesi širinu vidi kao na google Keep.Vidi kako se mijenja širina,visina i šta se mijenja
        String width = "40vw";
        String minWidth = "315px";
        notesTitle.setWidth(width);
        notesTitle.setMinWidth(minWidth);
        notesTitle.setValueChangeMode(ValueChangeMode.LAZY);

        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Type here:");
        textArea.setWidth(width);
        textArea.setMinWidth(minWidth);
        textArea.setMaxHeight("65vh");
        textArea.setValueChangeMode(ValueChangeMode.LAZY);
        // TODO dodaj maximum height kao na google keep pogledaj
        textArea.addFocusListener(e -> {
            notesTitle.setVisible(true);
        });

        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.setAlignItems(FlexComponent.Alignment.START);
        noteMenu.getStyle().setBorder("1px solid black");
        Icon imageIcon = new Icon("lumo", "photo");
        noteMenu.add(imageIcon);

        Button createNoteButton = new Button("Create note");
        createNoteButton.setEnabled(false);
        createNoteButton.addClickListener(click -> {
            createNewNote(notesTitle, textArea);
            //Daj signal da je doslo do promjene u brojevima
            ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(this,false));
        });
        notesTitle.addValueChangeListener(event -> {
            createNoteButton.setEnabled(!event.getValue().isBlank() || !textArea.getValue().isBlank());
        });
        textArea.addValueChangeListener(event -> {
            createNoteButton.setEnabled(!event.getValue().isBlank() || !notesTitle.getValue().isBlank());
        });

        this.add(notesTitle, textArea, createNoteButton);
    }

    private void createNewNote(TextField notesTitle, TextArea textArea) {
        String noteColor = "#FFFAF0";
        this.noteService.saveNote(new Note(notesTitle.getValue(), textArea.getValue(), 1L, noteColor));
        notesTitle.setValue("");
        textArea.setValue("");
        ComponentUtil.fireEvent(UI.getCurrent(), new NoteCreatedEvent(this, false));
    }
}
