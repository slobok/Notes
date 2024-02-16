package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.views.list.components.note.NoteEvents.NoteClickListeners;
import com.example.notes.views.list.components.note.NoteEvents.NoteComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;

import java.util.ArrayList;
import java.util.List;

public class NoteInTrash extends  NoteComponent{
    public NoteInTrash(Note note, NoteComponents noteComponents, NoteClickListeners noteClickListeners) {
        super(note, noteComponents, noteClickListeners);
    }

    @Override
    protected void addButtons() {
        super.addButtonsToNoteMenu(new ArrayList<Button>(List.of(getRestoreButton(), getDeleteButton())));
    }

    private Button getRestoreButton() {
        Button restore = new Button(new Icon("arrows-long-up"));
        restore.setTooltipText("Restore note");
        restore.addClickListener(klik -> {
            noteClickListeners.RestoreButtonListenerFun(note);
        });
        return restore;
    }

    private Button getDeleteButton( ) {
        Button deleteButton = new Button(new Icon("close"));
        deleteButton.setTooltipText("Delete note");
        deleteButton.addClickListener(click -> {
            noteClickListeners.deleteTrashListener(note);
        });
        return deleteButton;
    }
}