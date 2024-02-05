package com.example.notes.views.list;

import com.example.notes.data.Label;
import com.example.notes.services.FajlService;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.NotesContainer;
import com.example.notes.views.list.components.note.NoteComponent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

 @Route(value = "note/:label?", layout = MainLayout.class)
public class LabeledNotes extends Div implements BeforeEnterObserver {
    public String labelName;
    private final LabelService labelService;
    private final NoteService noteService;
    private final FajlService fajlService;
    LabeledNotes(NoteService noteService, LabelService labelService,FajlService fajlService) {
        this.noteService = noteService;
        this.labelService = labelService;
        this.fajlService = fajlService;
    }


    protected VerticalLayout getAllNotes() {
        NotesContainer notesContainer = new NotesContainer();
        Label label = labelService.findLabelByName(labelName);
        System.out.println(label.getName());
        noteService.getNotesByLabel(label).forEach(note -> {
            System.out.println(note.getText());
            notesContainer.add(new NoteComponent(note, noteService, labelService, fajlService));
        });
        return new VerticalLayout(notesContainer);
    }

     private VerticalLayout setHeaderWithParameter() {
        VerticalLayout vl = new VerticalLayout();
        vl.add(new H1(this.labelName));
        return vl;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        labelName = event.getRouteParameters().get("label").
                orElse("");
        removeAll();
        add(setHeaderWithParameter());
        add(getAllNotes());
    }
}