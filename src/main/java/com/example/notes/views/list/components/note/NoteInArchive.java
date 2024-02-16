package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.views.list.components.note.NoteEvents.NoteClickListeners;
import com.example.notes.views.list.components.note.NoteEvents.NoteComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.dom.Style;

import java.util.ArrayList;
import java.util.List;

public class NoteInArchive extends NoteComponent{
    public NoteInArchive(Note note, NoteComponents noteComponents, NoteClickListeners noteClickListeners) {
        super(note, noteComponents, noteClickListeners);
    }


    @Override
    protected void addButtons() {
        addButtonsToNoteMenu(new ArrayList<>(List.of(getUnarchivedButton(), super.toTrashButton())));
    }

    @Override
    protected Button pinButton(){
        Icon pinIcon = new Icon("pin");
        String pinIconColor = this.note.isPinned() ? "black" : "gray";
        pinIcon.setColor(pinIconColor);
        Button pinButton = new Button(pinIcon);
        pinButton.getStyle().setFloat(Style.FloatCss.RIGHT);
        String tooltipText = this.note.isPinned() ? "Unpin" : "Pin";
        pinButton.setTooltipText(tooltipText);
        pinButton.addClickListener(e -> {
           noteClickListeners.pinArchiveNoteListener(note);
        });
        return pinButton;
    }

    private Button getUnarchivedButton() {
        Button unarchiveButton = new Button(new Icon("arrow-circle-up"));
        unarchiveButton.setTooltipText("Unarchive note");
        unarchiveButton.addClickListener(klik -> {
            noteClickListeners.unarchiveNoteListener(note);
        });
        return  unarchiveButton;
    }

}
