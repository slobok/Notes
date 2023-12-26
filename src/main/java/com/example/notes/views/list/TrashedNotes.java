package com.example.notes.views.list;

import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.note.NoteInTrash;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

@Route(value = "trash",layout = MainLayout.class)
public class TrashedNotes extends NotesList  {

    TrashedNotes(NoteService noteService, LabelService labelService)  {
        super(noteService,labelService);
    }
    // TODO Ime ne valja
    @Override
    protected void addToPage(){
            add(
                    emptyTrashButton(),
                    getSearchField(),
                    getAllNotes()
            );
    }

    private Button emptyTrashButton() {
        Button emptyTrashButton = new Button("Empty trash");
        emptyTrashButton.addClickListener(click -> {
            this.noteService.deleteAllInTrash();
            this.updatePage();
        });
        return emptyTrashButton;
    }

    @Override
    protected VerticalLayout getAllNotes()  {
    VerticalLayout notesList = new VerticalLayout();
    notesList.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    this.noteService.getAllTrashedNotes(getSearch().getValue())
            .forEach(n -> {
                notesList.add(new NoteInTrash(n, noteService,labelService));
            }
    );
    return notesList;
}

    @Override
    protected void updatePage(){
        this.removeAll();
        this.add(
                this.getSearchField(),
                this.getAllNotes()
        );
    }
}