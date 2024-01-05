package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

public class NoteInTrash extends  NoteComponent{
    public NoteInTrash(Note note, NoteService noteService, LabelService labelService) {
        super(note, noteService,labelService);
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
            ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(this,false));
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
            ComponentUtil.fireEvent(UI.getCurrent(),new CountingNotesEvent(this,false));
            makeNotification("Note deleted",1200, Notification.Position.BOTTOM_START);
            this.removeFromParent();
        });
        return deleteButton;
    }
}