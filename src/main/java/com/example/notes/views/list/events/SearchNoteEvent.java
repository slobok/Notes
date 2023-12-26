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
     * @param searchString
     */
    public SearchNoteEvent(TextField source, String searchString) {
        super(source, false);
        this.searchString = searchString;
    }

    public String getSearchString(){
       return searchString;
    }
}
