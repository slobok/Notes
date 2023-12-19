package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

public class NoteInArchive extends NoteComponent{
    public NoteInArchive(Note note, NoteService noteService) {
        super(note, noteService);
    }

    @Override
    protected HorizontalLayout createNoteMenu() {
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        noteMenu.add(
                super.toTrashButton(),
                getUpdateChanges(),
                new Button(new Icon("ellipsis-v")),
                getUnarchivedButton()
        );
        return noteMenu;
    }

    private Button getUnarchivedButton() {
        Button unarchiveButton = new Button("Unarchive");
        unarchiveButton.addClickListener(clik -> {
            this.noteService.unarchiveNote(note.getId());
          //  this.updatePage();
        });
        return  unarchiveButton;
    }
}
