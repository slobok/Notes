package com.example.notes.views.list;

import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.vaadin.flow.router.Route;

@Route(value = "testVIew",layout = MainLayout.class)
public class ListViewNotes extends NotesList {

    ListViewNotes(NoteService noteService, LabelService labelService) {
        super(noteService, labelService);
    }
}
