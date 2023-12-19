package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

public class NoteInTrash extends  NoteComponent{
    public NoteInTrash(Note note, NoteService noteService) {
        super(note, noteService);
    }

    @Override
    protected HorizontalLayout createNoteMenu() {
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        noteMenu.add(
                getRestoreButton(),
                getDeleteButton()
        );
        return noteMenu;
    }

    private Button getRestoreButton() {
        Button restore = new Button(new Icon("arrows-long-up"));
        restore.setTooltipText("Restore note");
        restore.addClickListener(klik -> {
            this.noteService.restoreNote(note.getId());
            makeNotification("Note restored",1200, Notification.Position.BOTTOM_START);
            this.removeFromParent();
        });
        return restore;
    }

    private Button getDeleteButton() {
        Button deleteButton = new Button(new Icon("close"));
        deleteButton.setTooltipText("Delete note");
        deleteButton.addClickListener(click -> {
            this.noteService.deleteNote(note);
            makeNotification("Note deleted",1200, Notification.Position.BOTTOM_START);
            this.removeFromParent();
           // this.updatePage();
        });
        return deleteButton;
    }
}