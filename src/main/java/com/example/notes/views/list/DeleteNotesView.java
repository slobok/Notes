package com.example.notes.views.list;

import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("delete-note")
public class DeleteNotesView extends VerticalLayout {
    private final NoteService noteService;
    DeleteNotesView(NoteService noteService){
        this.noteService = noteService;
        Button deleteNotesButton = new Button("Delete all Notes");
        deleteNotesButton.addClickListener(event -> {
        this.noteService.deleteAllNotes();
        });
        add(deleteNotesButton);
    }
}
