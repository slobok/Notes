package com.example.notes.views.list.events;


import com.example.notes.data.Note;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

import java.util.List;

public class SelectNoteEvent extends ComponentEvent<Component> {
    private Long noteId;
    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source     the source component
     * @param fromClient <code>true</code> if the event originated from the client
     *
     *                    side, <code>false</code> otherwise
     */

    public SelectNoteEvent(Component source, boolean fromClient, Long noteId) {
        super(source, fromClient);
        this.noteId = noteId;
    }
    public Long getNoteId(){
        return noteId;
    }
}
