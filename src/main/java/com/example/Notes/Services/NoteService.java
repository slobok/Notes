package com.example.Notes.Services;

import com.example.Notes.Data.Note;
import com.example.Notes.Repository.NoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes(String searchText) {
        if (searchText.isEmpty() || searchText == null) {
            return this.noteRepository.findByIsTrashedAndIsArchived(0,0);
        }
        return this.noteRepository.search(searchText,  0,0);
    }


    public void saveNote(Note note) {
        try {
            this.noteRepository.save(note);
        } catch (Exception e) {
            System.out.println(note);
            throw new IllegalStateException("Nesto nije u redu");
        }

    }

    public void deleteNote(Note note) throws IllegalArgumentException {
        if (note == null) {
            throw new IllegalArgumentException("Note can not be null");
        }
        this.noteRepository.delete(note);
    }
   @Transactional
    public void updateStateIsTrashed(Long id) throws Exception {
        Optional<Note> optionalNote = this.noteRepository.findById(id);
        if (optionalNote.isEmpty()) {
            throw new IllegalStateException("Note does not exits");
        } else {
            optionalNote.get().setTrashed(1);
        }
    }
    @Transactional
    public void updateStateIsArchived(Long id) {
        Note note = this.noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Note with id " + "not founc"));
        note.setArchived(1);
    }

    public List<Note> getAllTrashedNotes(String searchText) {
        if(searchText.isEmpty() || searchText == null){
            return this.noteRepository.findByIsTrashed(1);
        }
        else{
           return this.noteRepository.search(searchText,1,0);
        }
    }


    @Transactional
    public void moveToTrash(Note note){
        Note n = this.noteRepository.findById(note.getId())
                .orElseThrow(() ->  new IllegalStateException("Not found note"));
     n.setTrashed(1);
}

    public List<Note> getAllArchivedNotes(String searchText) {
        if(searchText.isEmpty() || searchText == null){
            return this.noteRepository.findByIsTrashedAndIsArchived(0,1);
        }
        else {
            return this.noteRepository.search(searchText,0,1);
        }
    }

    public void deleteAll() {
        this.noteRepository.deleteAll(this.noteRepository.findByIsTrashed(1));
    }
    @Transactional
    public void setArchived(Long id){

    }

    @Transactional
    public void restoreNote(Long id) {
        Note note = this.noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Note with id:" + id + " not found"));
        note.setTrashed(0);
    }
    @Transactional
    public void unarchiveNote(Long id) {
        Note note = this.noteRepository.findById(id)
                .orElseThrow(() ->  new IllegalStateException("Note with id: " + id + " not found"));
        note.setArchived(0);
    }
}
