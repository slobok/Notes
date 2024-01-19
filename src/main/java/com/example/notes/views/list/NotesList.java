package com.example.notes.views.list;
import com.example.notes.data.Note;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.NotesGeneratorBox;
import com.example.notes.views.list.components.NewNoteForm;
import com.example.notes.views.list.components.NotesContainer;
import com.example.notes.views.list.components.note.NoteComponent;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.example.notes.views.list.events.PinNoteEvent;
import com.example.notes.views.list.events.SearchNoteEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
public class NotesList extends VerticalLayout  {
    protected NoteService noteService;
    protected LabelService labelService;
    private TextField search = new TextField();
    private final NewNoteForm newNoteForm;
    protected H3 message;

    NotesList(NoteService noteService, LabelService labelService) {
        this.labelService = labelService;
        this.noteService = noteService;
        this.newNoteForm = new NewNoteForm(noteService);
        createMessage();
        addComponentsToPage();
        //Todo promijeni imena ovih listenera, dobro da ona kazuju šta se dešava a ne da ja kucam tekst
        searchListener();
        listenerForPinNoteEvent();
        listenerToForAddingNewNote();
    }

    private void listenerForPinNoteEvent() {
        ComponentUtil.addListener(UI.getCurrent(), PinNoteEvent.class, (ComponentEventListener<PinNoteEvent>) event ->  {
            updatePage();
        });
    }

    private void listenerToForAddingNewNote() {
        ComponentUtil.addListener(UI.getCurrent(), CountingNotesEvent.class, (ComponentEventListener<CountingNotesEvent>) event ->  {
            updatePage();
        });
    }

    private void searchListener() {
        ComponentUtil.addListener(UI.getCurrent(), SearchNoteEvent.class, (ComponentEventListener<SearchNoteEvent>) event-> {
            search.setValue(event.getSearchString());
            updatePage();
        });
    }

    protected void addComponentsToPage() {
        this.add(
                newNoteForm,
                getAllNotes(),
                message,
                new NotesGeneratorBox(noteService)
        );
    }

    public TextField getSearch() {
        return search;
    }

    protected void createMessage(){
        message = new H3("Notes you add appear here:");
        message.setVisible(false);
        message.getStyle().setColor("gray");
        message.getStyle().setMargin("auto auto");
        }

    // Metoda koja dovlaci iz baze sve notese
    protected VerticalLayout getAllNotes() {
        return doGetAllNotes(search.getValue());
    }

    private VerticalLayout doGetAllNotes(String searchString) {
        // Kontejner u kome skladištim sve komponent all notes
        VerticalLayout allNotesLayout = new VerticalLayout();
        // Komponentu dvije grupe podijeljene pinned u unpinnded.
        // NotesContainer komponenta naparvljena u te svrhe
        NotesContainer notesContainer = new NotesContainer(new H6("Others"));

        notesContainer.getTitle().setVisible(false);
        NotesContainer pinnedNotesLayout = new NotesContainer(new H6("Pinned"));
        pinnedNotesLayout.setVisible(false);
        List<Note> allNotes = this.noteService.getAllNotes(searchString);

        if(allNotes.isEmpty()){
            message.setVisible(true);
        }
        List<Note> pinnedNotes = allNotes.stream().filter(note -> note.isPinned()).toList();
        List<Note> otherNotes = allNotes.stream().filter(note -> !note.isPinned()).toList();



        pinnedNotes.forEach(note -> {
            pinnedNotesLayout.setVisible(true);
            pinnedNotesLayout.add(new NoteComponent(note, noteService, labelService));
        });

        otherNotes.forEach(note -> {
            if(pinnedNotesLayout.isVisible()) notesContainer.getTitle().setVisible(true);
            notesContainer.add(new NoteComponent(note, noteService, labelService));
        });

        allNotesLayout.add(pinnedNotesLayout, notesContainer);

        return allNotesLayout;
    }

    protected void updatePage() {
        this.removeAll();
        this.add(
                newNoteForm,
                this.getAllNotes()
        );
    }
}