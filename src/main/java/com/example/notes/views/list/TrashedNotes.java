package com.example.notes.views.list;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.note.CreatingNewNoteComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "trash",layout = MainLayout.class)
public class TrashedNotes extends NotesList  {
//  U konstruktoru pozvan super(noteService)
//  a izmjenjena metoda addToConstructor,
//  kako ne bih prikazivao komponentu za dodavanje novog notesa

    TrashedNotes(NoteService noteService)  {
        super(noteService);
    }

    @Override
    protected void addToConstructor(){
            add(
                    emptyTrashButton(),
                    getSearchField(),
                    getAllNotes()
            );
    }
    private Button emptyTrashButton() {
        Button emptyTrashButton = new Button("Empty trash");
        emptyTrashButton.addClickListener(click -> {
            this.noteService.deleteAll();
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
                shoeNoteInNotesList(n, notesList);
            }
    );
    return notesList;
}

    private void shoeNoteInNotesList(Note n, VerticalLayout notesList) {
        //TODO treba napraviti komponentu koja je NoteComponent https://vaadin.com/docs/latest/create-ui/creating-components/basic

        //TODO notesList.add(new NoteComponent(n));

        VerticalLayout note = new VerticalLayout();
        note.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        note.getStyle().setBoxShadow("1px 1px 3px  linen");
        note.setWidth("30%");
        note.setMargin(true);

        TextField notesTitle = new TextField();
        notesTitle.setValue(n.getTitle());
        notesTitle.setLabel("Title");
        notesTitle.getStyle().setMargin("0");

        TextArea notesText = new TextArea();
        notesText.setValue(n.getText());
        notesText.setLabel("Notes text");
        notesText.getStyle().setMargin("0");

        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(click -> {
            this.noteService.deleteNote(n);
            this.updatePage();
        });

        Button restore = new Button("Restore");
        restore.addClickListener(klik -> {
            this.noteService.restoreNote(n.getId());
            this.updatePage();
        });
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.add(deleteButton, restore);
        note.add(notesTitle, notesText, noteMenu);
        notesList.add(note);
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
