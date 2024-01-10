package com.example.notes.views.list.components.mainlayout.drawer;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Style;

public class NotesNumber extends Span {

    public NotesNumber(){
        stylizeThisComponent();
    }
    public NotesNumber(String number){
        this.setText(number);
        stylizeThisComponent();
    }

    public void stylizeThisComponent(){
        this.getStyle().setTextAlign(Style.TextAlign.CENTER);
    }
}
