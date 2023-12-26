package com.example.notes.views.list;

import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.note.NoteInArchive;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

@Route(value = "archive",layout = MainLayout.class)
public class ArchivedNotes extends TrashedNotes {

    ArchivedNotes(NoteService noteService, LabelService labelService) {
        super(noteService, labelService);
    }

    @Override
    protected void addToPage(){
             add(
                     getSearchField(),
                     getAllNotes()
             );
    }
    @Override
    protected VerticalLayout getAllNotes()  {
        VerticalLayout notesList = new VerticalLayout();
        notesList.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        String searchText = getSearch().getValue();
        this.noteService.getAllArchivedNotes(searchText)
                .forEach(n -> {
                    notesList.add(new NoteInArchive(n, noteService, labelService));
                });
        return notesList;
    }
}