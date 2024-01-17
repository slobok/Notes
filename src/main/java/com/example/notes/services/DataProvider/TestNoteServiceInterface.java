package com.example.notes.services.DataProvider;

import com.example.notes.data.Note;

import java.util.List;

public interface TestNoteServiceInterface {
    List<Note> fetchNote(int offset, int limit);
    int getNoteXCount();
}
