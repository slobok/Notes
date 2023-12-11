package com.example.Notes.views.List;

import com.example.Notes.Data.Note;
import com.example.Notes.Services.NoteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

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
    this.noteService.getAllTrashedNotes(getSearch().getValue()).forEach(n -> {

                VerticalLayout note = new VerticalLayout();
                note.getStyle().setDisplay(Style.Display.INLINE_BLOCK);

                note.setWidth("30%");
                note.setMargin(true);
                note.getStyle().setBorder("1px solid black");

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

                Button updateChanges = new Button("Save");
                updateChanges.addClickListener(click -> {
                    Note noteToUpdate = new Note();
                    noteToUpdate.setId(n.getId());
                    noteToUpdate.setTitle(notesTitle.getValue());
                    noteToUpdate.setText(notesText.getValue());
                    noteToUpdate.setCreatedByUser(n.getCreatedByUser());
                    noteToUpdate.setIsArchived(n.getIsArchived());
                    noteToUpdate.setIsTrashed(n.getIsTrashed());
                    this.noteService.saveNote(noteToUpdate);
                    this.updatePage();
                });

                Button restore = new Button("Restore");
                restore.addClickListener(klik -> {
                    this.noteService.restoreNote(n.getId());
                    this.updatePage();
                });
                HorizontalLayout noteMenu = new HorizontalLayout();
                noteMenu.add(deleteButton, updateChanges, restore);
                note.add(notesTitle, notesText, noteMenu);
                notesList.add(note);
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
