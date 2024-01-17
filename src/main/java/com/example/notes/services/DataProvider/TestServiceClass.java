package com.example.notes.services.DataProvider;

import com.example.notes.data.Note;
import com.example.notes.repository.NoteRepository;
import com.example.notes.services.NoteService;

import java.util.List;

public class TestServiceClass implements TestNoteServiceInterface {

   private final NoteRepository noteRepository;
   private final NoteService noteService;
   TestServiceClass(NoteRepository noteRepository, NoteService noteService){
       this.noteRepository = noteRepository;
       this.noteService = noteService;
   }

    @Override
    public List<Note> fetchNote(int offset, int limit) {
        return this.noteRepository.myQuery(offset, limit);
    }

    @Override
    public int getNoteXCount() {
        return noteService.countNotes();
    }
}

