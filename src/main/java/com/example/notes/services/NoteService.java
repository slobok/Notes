package com.example.notes.services;

import com.example.notes.data.Note;
import com.example.notes.repository.NoteRepository;
import com.example.notes.exceptions.ErrorCode;
import com.example.notes.exceptions.NotesAppException;
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

    public Note findById(Long id){
        return  this.noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Not found Note with id" + id));
    }

    public List<Note> getAllNotes(String searchText) {
        if (searchText==null || searchText.isEmpty()) {
            return this.noteRepository.findByIsTrashedAndIsArchived(0, 0);
        }
        return this.noteRepository.search(searchText, 0, 0);
    }

    public void saveNote(Note note) {
        try {
            this.noteRepository.save(note);
        } catch (Exception e) {
            throw new NotesAppException("Greska prilikom skladistenja biljeske", ErrorCode.SAVING_NOTE_ERROR)
                    .set("note", note);
        }

    }

    public void deleteNote(Note note) throws IllegalArgumentException {
        if (note == null) {
            throw new IllegalArgumentException("Note can not be null");
        }
        this.noteRepository.delete(note);
    }

    @Transactional
    public void archiveNote(Long noteId) {
        Note note = this.noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalStateException("Note with id " +noteId+ "not found"));
        note.setIsArchived(1);
    }

    public List<Note> getAllTrashedNotes(String searchText) {
        if (searchText == null || searchText.isEmpty()) { //TODO
            return this.noteRepository.findByIsTrashed(1);
        } else {
            return this.noteRepository.search(searchText, 1, 0);
        }
    }

    @Transactional
    public void moveToTrash(Note note) {
        Note n = this.noteRepository.findById(note.getId())
                .orElseThrow(() -> new IllegalStateException("Not found note"));
        n.setIsTrashed(1);
    }

    public List<Note> getAllArchivedNotes(String searchText) {
        if (searchText.isEmpty() || searchText == null) { //TODO
            return this.noteRepository.findByIsTrashedAndIsArchived(0, 1);
        } else {
            return this.noteRepository.search(searchText, 0, 1);
        }
    }

    public void deleteAll() {
        this.noteRepository.deleteAll(this.noteRepository.findByIsTrashed(1));
    }

    @Transactional
    public void restoreNote(Long id) {
        Note note = this.noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Note with id:" + id + " not found"));
        note.setIsTrashed(0);
    }

    @Transactional
    public void unarchiveNote(Long id) {
        Note note = this.noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Note with id: " + id + " not found"));
        note.setIsArchived(0);
    }

    public void setIsInChechBoxSyle(Note n) {
    }

    @Transactional
    public void togglePin(Long id) {
        Note note = this.noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Not found note"));
        note.changePinned();
    }

    @Transactional
    public void unpinNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Note with id" + id + "not found"));
        note.setPinned(false);
    }


}
