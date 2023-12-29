package com.example.notes.views.list.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.textfield.TextField;

public class SearchNoteEvent extends ComponentEvent<TextField> {

   private final String searchString;
    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source       the source component
     */
    public SearchNoteEvent(TextField source, boolean fromClient, String searchString) {
        super(source, fromClient);
        this.searchString = searchString;
    }

    public String getSearchString(){
       return searchString;
    }
}