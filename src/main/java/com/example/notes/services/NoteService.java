package com.example.notes.services;

import com.example.notes.data.Label;
import com.example.notes.data.Note;
import com.example.notes.repository.LabelRepository;
import com.example.notes.repository.NoteRepository;
import com.example.notes.exceptions.ErrorCode;
import com.example.notes.exceptions.NotesAppException;
import com.github.javafaker.Faker;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final LabelRepository labelRepository;

    public NoteService(NoteRepository noteRepository, LabelRepository labelRepository, LabelService labelService) {
        this.noteRepository = noteRepository;
        this.labelRepository = labelRepository;
    }

    public List<Note> getAll() {
        return this.noteRepository.findAll();
    }

    //TODO popravi funkciju ispod
    @Transactional
    public void addLabelToNote(Note note, Label label) {
        Optional<Note> optionalNote = noteRepository.findById(note.getNoteId());
        if (optionalNote.isEmpty()) {
            throw new IllegalArgumentException("Not found note with id" + note.getNoteId());
        }
        Optional<Label> optionalLabel = labelRepository.findById(label.getLabelId());
        if (optionalLabel.isEmpty()) {
            throw new IllegalArgumentException("Not found label with id " + label.getLabelId());
        }
        optionalNote.get().setLabel(new ArrayList<>(List.of(optionalLabel.get())));
        optionalLabel.get().getLabeledNotes().add(optionalNote.get());
        this.noteRepository.save(optionalNote.get());
    }

    @Transactional
    public void removeLabelSFromNote(Note note, List<Label> labelsToRemove) {
        Note note1 = this.noteRepository.findById(note.getNoteId()).
                orElseThrow(() -> new IllegalArgumentException("Not found note with id " + note.getNoteId()));
        note1.getLabel().removeAll(labelsToRemove);
        labelsToRemove.forEach(label -> {
            Label label1 = this.labelRepository.findById(label.getLabelId())
                    .orElseThrow(() -> new RuntimeException("Not found label with id " + label.getLabelId()));
            label1.getLabeledNotes().remove(note1);
        });
        this.noteRepository.save(note1);
    }

    public void removeLabelFromNote(Note note, Label label) {
        Optional<Note> optionalNote = noteRepository.findById(note.getNoteId());
        if (optionalNote.isEmpty()) {
            throw new IllegalArgumentException("Not found note with id" + note.getNoteId());
        }
        Optional<Label> optionalLabel = labelRepository.findById(label.getLabelId());
        if (optionalLabel.isEmpty()) {
            throw new IllegalArgumentException("Not found label with id " + label.getLabelId());
        }
        optionalNote.get().getLabel().remove(label);
    }

    public Note findById(Long id) {
        return this.noteRepository.findById(id)
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
                .orElseThrow(() -> new IllegalStateException("Note with id " + noteId + " not found !!!"));
        note.setIsArchived(1);
    }

    @Transactional
    public void archiveMoreNotes(Set<Long > selectedNotes){
        selectedNotes.forEach(this::archiveNote);
    }


    @Transactional
    public void moveToTrash(Note note) {
        Note n = this.noteRepository.findById(note.getNoteId())
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
    public void pinNote(Long noteId){
        Note note = this.noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Not dound note with id " + noteId));
        note.setPinned(true);
    }

    @Transactional
    public void pinManyNotes(Set<Long> noteSet){
        noteSet.forEach(this::pinNote);
    }

    @Transactional
    public void unpinNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Note with id" + id + "not found"));
        note.setPinned(false);
    }

    public long countAllNotes() {
        return (int) noteRepository.count();
    }

    public int countNotesInTrash() {
        return noteRepository.countByIsTrashed(1);
    }

    public int countNotesInArchive() {
        return noteRepository.countByIsTrashedAndIsArchived(0, 1);
    }

    public int countNotes() {
        return noteRepository.countByIsTrashedAndIsArchived(0, 0);
    }

    public String getNotesColor(Long notesId) {
        Note note = this.noteRepository.findById(notesId)
                .orElseThrow(() -> new IllegalStateException("Note with id " + notesId + " not found"));
        return note.getNoteColor();
    }

    @Transactional
    public void setNotesColor(String noteColor, Long notesId) {
        Note note = this.noteRepository.findById(notesId)
                .orElseThrow(() -> new IllegalStateException("Note with id " + notesId + " not found"));
        note.setNoteColor(noteColor);
    }

    @Transactional
    public List<Label> getNoteLabels(Long noteId) {
        Note note = this.noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalStateException("Note with id:" + noteId + "not found"));
        return new ArrayList<>(note.getLabel());
    }
    
    public void addManyNotesToDatabase(Integer numberOfNotes){
        for(int i = 0; i < numberOfNotes; i++){
            Faker faker = new Faker();
            ArrayList<Label> labelList = new ArrayList<>();
            Note note = new  Note(
                    faker.animal().name(),
                    faker.lorem().characters(40),
                    1L,
                    faker.color().name());
            String labelName = faker.book().title();
            while (labelRepository.findByName(labelName).isPresent()){
                labelName = faker.book().title();
            }
            Label label = new Label(labelName);
            note.setLabel(new ArrayList<>(List.of(label)));
            label.setLabeledNotes(new ArrayList<>(new ArrayList<>(List.of(note))));
            labelRepository.save(label);
            noteRepository.save(note);

  /*          Note note = noteRepository.save(new Note(
                    faker.animal().name(),
                    faker.lorem().characters(40),
                    1L,
                    faker.color().name()));
                    Label label  = labelRepository.save(new Label(faker.book().title()));
                    Label label2 = labelRepository.findById(label.getLabelId())
                                    .orElseThrow(()->  new IllegalArgumentException("Not found label"));
                    note.getLabel().add(label2);
                    label2.getLabeledNotes().add(note);
                    labelList.add(label2);
                    System.out.println("labelList" +  labelList);

                    addLabelsToNote(note, labelList);*/
        }
    }

    @Transactional
    public void addLabelsToNote(Note note, List<Label> labels) {
        Note note1 = this.noteRepository.findById(note.getNoteId()).
                orElseThrow(() -> new IllegalArgumentException("Not found note with id " + note.getNoteId()));
        note1.getLabel().addAll(labels);

        labels.forEach(label -> {
            Label l = this.labelRepository.findById(label.getLabelId())
                    .orElseThrow(() -> new IllegalArgumentException("Not found label with id " + label.getLabelId()));
            l.getLabeledNotes().add(note1);
            labelRepository.save(l);
            noteRepository.save(note1);
        });
        noteRepository.save(note1);
    }

    public List<Note> getNotesWithOffsetAndLimit(int offset,int limit){
        return this.noteRepository.myQuery(offset, limit);
    }

    public void deleteAllNotes(){
        try {
            this.noteRepository.deleteAll();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    @Transactional
    public void addLabelAndNote(Note note, String labelName){
        Label label = labelRepository.findByName(labelName)
                        .orElseThrow(() -> new IllegalArgumentException(""));
        label.setLabeledNotes(new ArrayList<>(List.of(note)));
        note.setLabel(new ArrayList<>(List.of(label)));
        noteRepository.save(note);
    }

    public  List<Note> getNotesByLabel(Label label){
        System.out.println(label.getName());
        System.out.println(new ArrayList<>(noteRepository.findByLabel(label)));
        return this.noteRepository.findByLabel(label);
    }
}