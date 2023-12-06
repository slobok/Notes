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
        try{
            this.noteRepository.save(note);
        }
        catch (Exception e){
            System.out.println(note);
            throw new IllegalStateException("Nesto nije u redu");
        }

    }

    public void deleteNote(Note note) throws IllegalArgumentException {
        if (note == null){
            throw new IllegalArgumentException("Note can not be null");
        }
        this.noteRepository.delete(note);
    }

   public void updateNote(Note note){

   }

}
