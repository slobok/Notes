package com.example.notes.views.list.events;

import com.example.notes.views.list.components.note.NoteComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;

public class PinNoteEvent extends ComponentEvent<Button>{
    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source     the source component
     * @param fromClient <code>true</code> if the event originated from the client
     *                   side, <code>false</code> otherwise
     */
    public PinNoteEvent(Button source, boolean fromClient) {
        super(source, fromClient);
    }
}
