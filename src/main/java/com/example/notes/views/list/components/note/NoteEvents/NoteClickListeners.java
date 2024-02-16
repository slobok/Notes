package com.example.notes.views.list.components.note.NoteEvents;

import com.example.notes.data.Fajl;
import com.example.notes.data.Note;
import com.example.notes.services.FajlService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.example.notes.views.list.events.PinNoteEvent;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

@Component
public class NoteClickListeners {
    private final NoteService noteService;
    private final FajlService fajlService;
    NoteClickListeners(NoteService noteService, FajlService fajlService){
        this.noteService = noteService;
        this.fajlService = fajlService;
    }

    public void pinButtonClick(Note note) {
        this.noteService.togglePin(note.getNoteId());
        String message = note.isPinned() ? "Note unpinned" : "Note pinned";
        makeNotification(message, 1200, Notification.Position.BOTTOM_START);
        ComponentUtil.fireEvent(UI.getCurrent(), new PinNoteEvent(new Button(), false));
    }

    public void  noteMoveToTrash(Note note) {
        toTrash(note);
        ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(new Button(), false));
            makeNotification(
                    "Note moved to Trash",
                    1000,
                    Notification.Position.BOTTOM_START);
    }

    public HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Input, String>> getValueChangeEventValueChangeListener(com.vaadin.flow.component.Component component, Note note) {
        return event -> {
            component.getStyle().setBackground(event.getValue());
            note.setNoteColor(event.getValue());
            this.noteService.saveNote(note);
        };
    }

    public void pinButtonClickListener(Note note) {
        this.noteService.togglePin(note.getNoteId());
        String message = note.isPinned() ? "Note unpinned" : "Note pinned";
        makeNotification(message,1200, Notification.Position.BOTTOM_START);
        ComponentUtil.fireEvent(UI.getCurrent(), new PinNoteEvent(new Button(),false));
    }

    public void archiveButtonClickListener(Note note) {
        toArchiveNote(note);
        ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(new Button(), false));
        String message = note.isPinned() ? "Note archived and unpinned" : "Note archived";
        makeNotification(
                message,
                1000,
                Notification.Position.BOTTOM_START
        );
    }

    public void saveNotesText(Note note,String notesTextValue) {
        note.setText(notesTextValue);
        //Todo mozda bolje praviti novu f - ju sa mozda sa
        //todo samo teskt da azurira
        this.noteService.saveNote(note);
    }

    public void saveNotesTitle(Note note,String notesTileValue) {
        note.setTitle(notesTileValue);
        this.noteService.saveNote(note);
    }

    public void toArchiveNote(Note note) {
        this.noteService.archiveNote(note.getNoteId());
        this.noteService.unpinNote(note.getNoteId());
    }

    private void toTrash(Note note) {
        this.noteService.moveToTrash(note);
        this.noteService.unpinNote(note.getNoteId());
    }

    protected void makeNotification(String notificationText,int durationInMilliseconds,Notification.Position position ){
        Notification notification =  Notification.show(notificationText);
        notification.setDuration(durationInMilliseconds);
        notification.setPosition(position);
    }

    public Note saveNoteToDnAndGet(Note note) {
        this.noteService.saveNote(note);
        note = this.noteService.findById(note.getNoteId());
        return note;
    }

    public void RestoreButtonListenerFun(Note note) {
        this.noteService.restoreNote(note.getNoteId());
        ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(new Button(),false));
        makeNotification("Note restored",1200, Notification.Position.BOTTOM_START);
    }

    public void deleteTrashListener(Note note) {
        this.noteService.deleteNote(note);
        ComponentUtil.fireEvent(UI.getCurrent(),new CountingNotesEvent(new Button(),false));
        makeNotification("Note deleted",1200, Notification.Position.BOTTOM_START);
    }

    public void pinArchiveNoteListener(Note note) {
        this.noteService.unarchiveNote(note.getNoteId());
        this.noteService.togglePin(note.getNoteId());
        String message = "Note unarchived and pinned";
        makeNotification(message,1200, Notification.Position.BOTTOM_START);
        ComponentUtil.fireEvent(UI.getCurrent(),new PinNoteEvent(new Button(),false));
    }

    public void unarchiveNoteListener(Note note) {
        this.noteService.unarchiveNote(note.getNoteId());
        ComponentUtil.fireEvent(UI.getCurrent(),new CountingNotesEvent(new Button(),false));
        makeNotification(
                "Note unarchived",
                1200,
                Notification.Position.BOTTOM_START);
    }

    public void generateHugeNumberOfFiles(Note note) throws FileNotFoundException {
        File file  = new File("D:\\Slobo\\NotesApp\\Notes\\Zbirka zadataka.pdf");
        ArrayList<InputStream> inputStreams = new ArrayList<>();
        int number = 10000;
        for (int i = 0; i < number; ++i){
            inputStreams.add(new FileInputStream(file));
        }

        for(InputStream inputStream : inputStreams) {
            Fajl fajl = new Fajl();
            fajl.setFileName("File name");
            fajl.setNote(note);
            fajl.setFileSize(file.getTotalSpace());
            this.fajlService.saveFileAndFileData(fajl, inputStream,file.getTotalSpace());
        }
    }
}