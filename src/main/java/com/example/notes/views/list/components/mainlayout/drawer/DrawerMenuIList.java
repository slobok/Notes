package com.example.notes.views.list.components.mainlayout.drawer;

import com.example.notes.services.NoteService;
import com.example.notes.views.list.ArchivedNotes;
import com.example.notes.views.list.NotesList;
import com.example.notes.views.list.TrashedNotes;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;


public class DrawerMenuIList extends VerticalLayout {

    // numbers of notes should be updated
    Integer numberOfNotesInTrash;
    Integer numberOfNotesInArchive;
    Integer numberOfNotes;
    private final NoteService noteService;
    public DrawerMenuIList(NoteService noteService){
        this.noteService = noteService;
        setNumberOfNotes();
       // Updating number of notes
        ComponentUtil.addListener(UI.getCurrent(), CountingNotesEvent.class, event -> {
           setNumberOfNotes();
           removeAll();
           addToDrawer();
        });
      addToDrawer();

    }

    private void setNumberOfNotes(){
        numberOfNotes = noteService.countNotes();
        numberOfNotesInArchive = noteService.countNotesInArchive();
        numberOfNotesInTrash = noteService.countNotesInTrash();
    }

    private void addToDrawer(){
        add(
                notesLinkAndNumber(),
                archivedLink(),
                trashLink()
        );
    }

    private HorizontalLayout notesLinkAndNumber(){
        NotesNumber number = new NotesNumber(numberOfNotes.toString());
        HorizontalLayout horizontalLayout = new HorizontalLayout(
                new RouterLink("Notes", NotesList.class),
                number
        );
        horizontalLayout.setVerticalComponentAlignment(Alignment.CENTER);
        return  horizontalLayout;
    }
    private HorizontalLayout archivedLink(){
        NotesNumber number = new NotesNumber(numberOfNotesInArchive.toString());
        HorizontalLayout horizontalLayout = new HorizontalLayout(
                new RouterLink("Archive", ArchivedNotes.class),
                number

        );
        horizontalLayout.setVerticalComponentAlignment(Alignment.CENTER);
        return  horizontalLayout;
    }

    private HorizontalLayout trashLink(){
        NotesNumber number = new NotesNumber(numberOfNotesInTrash.toString());
        HorizontalLayout horizontalLayout = new HorizontalLayout(
                new RouterLink("Trash", TrashedNotes.class),
                number

        );
        horizontalLayout.setVerticalComponentAlignment(Alignment.CENTER);
        return  horizontalLayout;
    }
}