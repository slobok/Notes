package com.example.notes.views.list;

import com.example.notes.data.Label;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.NewNoteForm;
import com.example.notes.views.list.components.NotesContainer;
import com.example.notes.views.list.components.note.NoteComponent;
import com.example.notes.views.list.components.note.NoteEvents.NoteClickListeners;
import com.example.notes.views.list.components.note.NoteEvents.NotesComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route(value = "note/:label?", layout = MainLayout.class)
public class LabeledNotes extends Div implements BeforeEnterObserver {
    public String labelName;
    private final LabelService labelService;
    private final NoteService noteService;
    private final NotesComponents noteComponents;
    private final NoteClickListeners noteClickListeners;
    public LabeledNotes(NoteService noteService, LabelService labelService, NotesComponents noteComponents, NoteClickListeners noteClickListeners1) {
        this.noteService = noteService;
        this.labelService = labelService;
        this.noteComponents = noteComponents;
        this.noteClickListeners = noteClickListeners1;
    }


    protected VerticalLayout getAllNotes() {
        NotesContainer notesContainer = new NotesContainer();
        Label label = null;
        try {
            label = labelService.findLabelByName(labelName);
        }
        catch (IllegalArgumentException e){

        }

        if(label != null){
            noteService.getNotesByLabel(label).forEach(note -> {
                notesContainer.add(new NoteComponent(note, noteComponents, noteClickListeners));
            });
            return new VerticalLayout(notesContainer);
        }
        else{
            return new VerticalLayout(new H2("Not found label with name" + labelName));
        }
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

        NewNoteForm newNoteForm = new NewNoteForm(noteService);
        newNoteForm.setLabel(labelName);
        add(newNoteForm);
        add(setHeaderWithParameter());
        add(getAllNotes());
    }
}