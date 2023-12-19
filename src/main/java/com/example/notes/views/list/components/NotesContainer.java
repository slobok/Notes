package com.example.notes.views.list.components;

import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;

public class NotesContainer extends VerticalLayout {
    private H6 title;

    public NotesContainer(){}
    public NotesContainer(H6 title){
        this.title = title;
        stylizeThisComponent();
        add(title);
    }

    public H6 getTitle() {
        return title;
    }
    public void setTitle(H6 title) {
        this.title = title;
    }

    private void stylizeThisComponent(){
        this.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    }
}