package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.repository.FileContentDbRepository;
import com.example.notes.services.FajlService;
import com.example.notes.services.FileContentService;
import com.example.notes.services.Helper.LobHelper;
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
import org.hibernate.SessionFactory;

public class NoteInArchive extends NoteComponent{
    public NoteInArchive(Note note, NoteService noteService, LabelService labelService, FajlService fajlService, SessionFactory sessionFactory, FileContentService fileContentService) {
        super(note, noteService, labelService, fajlService, sessionFactory, fileContentService);
    }

    @Override
    protected void addButtonsToNoteMenu(HorizontalLayout noteMenu) {
        noteMenu.add(
             //   saveChangesButton(),
                getUnarchivedButton(),
                super.toTrashButton()
        );
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
            this.noteService.unarchiveNote(this.note.getNoteId());
            this.noteService.togglePin(this.note.getNoteId());
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
            this.noteService.unarchiveNote(note.getNoteId());
            ComponentUtil.fireEvent(UI.getCurrent(),new CountingNotesEvent(this,false));
            makeNotification(
                    "Note unarchived",
                    1200,
                    Notification.Position.BOTTOM_START);
        });
        return  unarchiveButton;
    }
}
