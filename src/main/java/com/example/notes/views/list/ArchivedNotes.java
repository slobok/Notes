package com.example.notes.views.list;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.note.NoteEvents.NoteClickListeners;
import com.example.notes.views.list.components.note.NoteEvents.NoteComponents;
import com.example.notes.views.list.components.note.NoteInArchive;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "archive",layout = MainLayout.class)
public class ArchivedNotes extends TrashedNotes {

    ArchivedNotes(NoteService noteService, NoteComponents noteComponents, NoteClickListeners noteClickListeners) {
        super(noteService, noteComponents, noteClickListeners);
    }

    @Override
    protected void addComponentsToPage(){
             add(
                     getAllNotes()
             );
    }

    @Override
    protected List<Note> getPageNotes() {
        return noteService.getAllArchivedNotes(getSearch().getValue());
    }

    @Override
    protected void setNoteType(Note note, Div div) {
        div.add(new NoteInArchive(note, noteComponents, noteClickListeners));
    }
}