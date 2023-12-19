package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

public class NoteInTrash extends  NoteComponent{
    public NoteInTrash(Note note, NoteService noteService) {
        super(note, noteService);
    }


    @Override
    protected HorizontalLayout createNoteMenu() {
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        noteMenu.add(
                super.toTrashButton(note),
                getUpdateChanges(),
                new Button(new Icon("ellipsis-v")),
                getRestoreButton(),
                getDeleteButton()
        );
        return noteMenu;
    }

    private Button getRestoreButton() {
        Button restore = new Button("Restore");
        restore.addClickListener(klik -> {
            this.noteService.restoreNote(note.getId());
          //  this.updatePage();
        });
        return restore;
    }

    private Button getDeleteButton() {
        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(click -> {
            this.noteService.deleteNote(note);
           // this.updatePage();
        });
        return deleteButton;
    }

}
