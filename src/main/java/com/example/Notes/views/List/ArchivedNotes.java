package com.example.Notes.views.List;


import com.example.Notes.Data.Note;
import com.example.Notes.Services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

@Route(value = "archive",layout = MainLayout.class)
public class ArchivedNotes extends TrashedNotes {


    ArchivedNotes(NoteService noteService) {
        super(noteService);
    }

    @Override
    protected VerticalLayout getAllNotes()  {
        VerticalLayout notesList = new VerticalLayout();
        notesList.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.noteService.getAllArchivedNotes(getSearch().getValue()).forEach(n -> {

                    VerticalLayout note = new VerticalLayout();
                    note.getStyle().setDisplay(Style.Display.INLINE_BLOCK);

                    note.setWidth("30%");
                    note.setMargin(true);
                    note.getStyle().setBorder("1px solid black");

                    TextField notesTitle = new TextField();
                    notesTitle.setValue(n.getTitle());
                    notesTitle.setLabel("Title");
                    notesTitle.getStyle().setMargin("0");

                    TextField notesText = new TextField();
                    notesText.setValue(n.getText());
                    notesText.setLabel("Notes text");
                    notesText.getStyle().setMargin("0");


                    Button deleteButton = new Button("Move to trash");
                    deleteButton.addClickListener(click -> {
                        this.noteService.moveToTrash(n);
                        this.updatePage();
                    });

                    Button updateChanges = new Button("Save");
                    updateChanges.addClickListener(click -> {
                        Note noteToUpdate = new Note();
                        noteToUpdate.setId(n.getId());
                        noteToUpdate.setTitle(notesTitle.getValue());
                        noteToUpdate.setText(notesText.getValue());
                        noteToUpdate.setCreatedByUser(n.getCreatedByUser());
                        this.noteService.saveNote(noteToUpdate);
                        this.updatePage();
                    });

                    Button unarchiveButton = new Button("Unarchive");
                    unarchiveButton.addClickListener(clik -> {
                        this.noteService.unarchiveNote(n.getId());
                        this.updatePage();
                    });

                    note.add(notesTitle, notesText, deleteButton, updateChanges, unarchiveButton);
                    notesList.add(note);
                }
        );
        return notesList;
    }

}
