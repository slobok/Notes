package com.example.Notes.Services;

import com.example.Notes.Data.Note;
import com.example.Notes.Repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService  {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes(){
        return this.noteRepository.findAll();
    }

    public void saveNote(Note note){
        this.noteRepository.save(note);
    }


}
