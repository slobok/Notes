package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

public class NoteInArchive extends NoteComponent{
    public NoteInArchive(Note note, NoteService noteService, LabelService labelService) {
        super(note, noteService, labelService);
    }

    @Override
    protected HorizontalLayout createNoteMenu() {
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        noteMenu.add(
                saveChangesButton(),
                getUnarchivedButton(),
                super.toTrashButton()
        );
        return noteMenu;
    }

    private Button getUnarchivedButton() {
        Button unarchiveButton = new Button(new Icon("arrow-circle-up"));
        unarchiveButton.setTooltipText("Unarchive note");
        unarchiveButton.addClickListener(klik -> {
            this.noteService.unarchiveNote(note.getId());
            makeNotification(
                    "Note unarchived",
                    1200,
                    Notification.Position.BOTTOM_START);
            this.removeFromParent();
        });
        return  unarchiveButton;
    }
}
