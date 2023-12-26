package com.example.notes.services;

import com.example.notes.data.Label;
import com.example.notes.data.Note;
import com.example.notes.repository.LabelRepository;
import com.example.notes.repository.NoteRepository;
import com.example.notes.exceptions.ErrorCode;
import com.example.notes.exceptions.NotesAppException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final LabelRepository labelRepository;

    public NoteService(NoteRepository noteRepository, LabelRepository labelRepository) {
        this.noteRepository = noteRepository;
        this.labelRepository = labelRepository;
    }
    // Add label to note service

   //TODO popravi funkciju ispod
    @Transactional
    public void addLabelToNote(Note note,Label label){
        Optional <Note> optionalNote = noteRepository.findById(note.getId());
        if (optionalNote.isEmpty()){
            throw new IllegalArgumentException("Not found note with id" + note.getId());
        }
        Optional <Label> optionalLabel = labelRepository.findById(label.getId());
        if(optionalLabel.isEmpty()){
            throw new IllegalArgumentException("Not found label with id "  + label.getId());
        }
        optionalNote.get().getLabel().add(label);
    }

    @Transactional
    public void addLabelsToNote(Note note, Set <Label> labelsSet){
        Note note1 =  this.noteRepository.findById(note.getId()).
                orElseThrow(() -> new IllegalArgumentException("Not found note with id " + note.getId()));
        note1.setLabel(labelsSet);
    }

    public void removeLabelFromNote(Note note, Label label){
        Optional <Note> optionalNote = noteRepository.findById(note.getId());
        if (optionalNote.isEmpty()){
            throw new IllegalArgumentException("Not found note with id" + note.getId());
        }
        Optional <Label> optionalLabel = labelRepository.findById(label.getId());
        if(optionalLabel.isEmpty()){
            throw new IllegalArgumentException("Not found label with id "  + label.getId());
        }
        optionalNote.get().getLabel().remove(label);
    }



    public Note findById(Long id){
        return  this.noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Not found Note with id" + id));
    }

    public List<Note> getAllNotes(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return this.noteRepository.findByIsTrashedAndIsArchived(0, 0);
        }
        return this.noteRepository.search(searchText, 0, 0);
    }

    public List<Note> getAllTrashedNotes(String searchText) {
        if (searchText == null || searchText.isEmpty()) { //TODO
            return this.noteRepository.findByIsTrashed(1);
        } else {
            return this.noteRepository.search(searchText, 1, 0);
        }
    }

    public List<Note> getAllArchivedNotes(String searchText) {
        if (searchText == null || searchText.isEmpty()) { //TODO
            return this.noteRepository.findByIsTrashedAndIsArchived(0, 1);
        } else {
            return this.noteRepository.search(searchText, 0, 1);
        }
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
                .orElseThrow(() -> new IllegalStateException("Note with id " + noteId +  " not found !!!"));
        note.setIsArchived(1);
    }

    @Transactional
    public void moveToTrash(Note note) {
        Note n = this.noteRepository.findById(note.getId())
                .orElseThrow(() -> new IllegalStateException("Not found note"));
        n.setIsTrashed(1);
    }

    @Transactional
    public void restoreNote(Long id) {
        Note note = this.noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Note with id:" + id + " not found"));
        note.setIsTrashed(0);
    }

    public void deleteAllInTrash() {
        this.noteRepository.deleteAll(this.noteRepository.findByIsTrashed(1));
    }

    @Transactional
    public void unarchiveNote(Long id) {
        Note note = this.noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Note with id: " + id + " not found"));
        note.setIsArchived(0);
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