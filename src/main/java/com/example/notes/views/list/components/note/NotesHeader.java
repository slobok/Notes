package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;


public class NotesHeader extends HorizontalLayout {

    public NotesHeader(TextField notesTitle, Note note, NoteService noteService){
        add(notesTitle, getPinButton(note, noteService));
    }
    private void stylingThisComponent(){
        // this.getStyle().set
    }
    private Button getPinButton(Note note, NoteService noteService) {
        Icon pinIcon = new Icon("pin");
        String pinIconColor = note.isPinned() ? "black" : "gray";
        pinIcon.setColor(pinIconColor);
        Button pinButton = new Button(pinIcon);
        pinButton.getStyle().setFloat(Style.FloatCss.LEFT);
        String tooltipText = note.isPinned() ? "Unpin" : "Pin";
        pinButton.setTooltipText(tooltipText);
        pinButton.addClickListener(e -> {
            noteService.togglePin(note.getId());
            //  this.updatePage();
        });
        return pinButton;
    }

}
