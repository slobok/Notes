package com.example.notes.views.list;

import com.example.notes.services.NoteService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route(value = "note/:label?", layout = MainLayout.class)
public class LabeledNotes extends NotesList implements BeforeEnterObserver {

    public String labelName;
    LabeledNotes(NoteService noteService) {
        super(noteService);
       add(
            setHeaderWithParameter()
       );
    }

    private VerticalLayout setHeaderWithParameter() {
        VerticalLayout vl = new VerticalLayout();
        vl.add(new H1(this.labelName + " - test"));
        return vl;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        labelName = event.getRouteParameters().get("label").
                orElse("");
        System.out.println(labelName);
    }
}