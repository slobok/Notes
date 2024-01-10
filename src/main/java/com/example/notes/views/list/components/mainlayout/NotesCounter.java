package com.example.notes.views.list.components.mainlayout;

import com.example.notes.views.list.components.NotesContainer;
import com.vaadin.flow.component.html.Span;

public class NotesCounter extends Span {

    public NotesCounter(){
        stylizeThisComponent();
    }

    public NotesCounter(String number) {
        super(number);
        stylizeThisComponent();
    }

    private void stylizeThisComponent() {
        this.getElement().getThemeList().add("badge contrast pill");
        //this.getElement().setAttribute("aria-label", "12 unread messages");
    }
}
