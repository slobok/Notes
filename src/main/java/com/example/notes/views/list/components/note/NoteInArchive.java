package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.example.notes.views.list.events.PinNoteEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
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

    @Override
    protected Button getPinButton(){
        Icon pinIcon = new Icon("pin");
        String pinIconColor = this.note.isPinned() ? "black" : "gray";
        pinIcon.setColor(pinIconColor);
        Button pinButton = new Button(pinIcon);
        pinButton.getStyle().setFloat(Style.FloatCss.RIGHT);
        String tooltipText = this.note.isPinned() ? "Unpin" : "Pin";
        pinButton.setTooltipText(tooltipText);
        pinButton.addClickListener(e -> {
            this.noteService.unarchiveNote(this.note.getId());
            this.noteService.togglePin(this.note.getId());
            String message = "Note unarchived and pinned";
            makeNotification(message,1200, Notification.Position.BOTTOM_START);
            ComponentUtil.fireEvent(UI.getCurrent(),new PinNoteEvent(pinButton,false));
        });
        return pinButton;
    }


    private Button getUnarchivedButton() {
        Button unarchiveButton = new Button(new Icon("arrow-circle-up"));
        unarchiveButton.setTooltipText("Unarchive note");
        unarchiveButton.addClickListener(klik -> {
            this.noteService.unarchiveNote(note.getId());
            ComponentUtil.fireEvent(UI.getCurrent(),new CountingNotesEvent(this,false));
            makeNotification(
                    "Note unarchived",
                    1200,
                    Notification.Position.BOTTOM_START);
            this.removeFromParent();
        });
        return  unarchiveButton;
    }
}
