package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.views.list.components.note.NoteEvents.NoteClickListeners;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

public class NoteMenu extends HorizontalLayout {

    private final NoteClickListeners noteClickListeners;
    private final Note note;

    public NoteMenu(NoteClickListeners noteClickListeners, Note note) {
        this.noteClickListeners = noteClickListeners;
        this.note = note;
        getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    }
}
