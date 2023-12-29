package com.example.notes.views.list;

import com.example.notes.data.Note;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.NotesContainer;
import com.example.notes.views.list.components.note.NoteComponent;
import com.example.notes.views.list.events.SearchNoteEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
public class NotesList extends VerticalLayout  {
    protected NoteService noteService;
    protected LabelService labelService;
    private TextField search = new TextField();
    protected H3 message;
    NotesList(NoteService noteService,LabelService labelService) {
        this.labelService = labelService;
        this.noteService = noteService;
        createMessage();
        addComponentsToPage();

        // Register to events from the event bus
        ComponentUtil.addListener(UI.getCurrent(), SearchNoteEvent.class, (ComponentEventListener<SearchNoteEvent>) event-> {
            search.setValue(event.getSearchString());
            updatePage();
        });
    }

    protected void addComponentsToPage() {
        this.add(
                createNoteForm(),
                getAllNotes(),
                message
        );
    }

    public TextField getSearch() {
        return search;
    }

    protected void createMessage(){
        message = new H3("Notes appear hear");
        message.setVisible(true);
        message.getStyle().setColor("gray");
        message.getStyle().setMargin("auto auto");
        }

    private Component createNoteForm() {
        VerticalLayout newNote = new VerticalLayout();
        newNote.setAlignItems(Alignment.CENTER);
        newNote.getStyle().setBoxShadow("1px 1px 2px linen");

        TextField notesTitle = new TextField();
        notesTitle.setPlaceholder("Title");
        notesTitle.setVisible(false);
        notesTitle.setWidth("30%");

        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Type here");
        textArea.setWidth("30%");
        textArea.addFocusListener(e -> {
            notesTitle.setVisible(true);
        });

        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.setAlignItems(Alignment.START);
        noteMenu.getStyle().setBorder("1px solid black");
        Icon imageIcon = new Icon("lumo", "photo");
        noteMenu.add(imageIcon);

        Button createNote = new Button("Create note");
        createNote.addClickListener(click -> {
            createNewNote(notesTitle, textArea);
        });
        newNote.add(notesTitle, textArea, createNote);
        return newNote;
    }

    private void createNewNote(TextField notesTitle, TextArea textArea) {
        this.noteService.saveNote(new Note(notesTitle.getValue(), textArea.getValue(), 1L));
        notesTitle.setValue("");
        textArea.setValue("");
        this.updatePage();
    }

    // Metoda koja dovlaci iz baze sve notese
    protected VerticalLayout getAllNotes() {
        return doGetAllNotes(this.search.getValue());
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
        List<Note> pinnedNotes = allNotes.stream().filter(note -> note.isPinned()).toList();
        List<Note> otherNotes = allNotes.stream().filter(note -> !note.isPinned()).toList();

        pinnedNotes.forEach(note -> {
            this.message.setVisible(false);
            pinnedNotesLayout.setVisible(true);
            pinnedNotesLayout.add(new NoteComponent(note, noteService, labelService));
        });

        otherNotes.forEach(note -> {
            this.message.setVisible(false);
            if(pinnedNotesLayout.isVisible()) notesContainer.getTitle().setVisible(true);
            notesContainer.add(new NoteComponent(note, noteService, labelService));
        });

        allNotesLayout.add(pinnedNotesLayout, notesContainer);

        return allNotesLayout;
    }

    protected void updatePage() {
        this.removeAll();
        this.add(
                this.createNoteForm(),
                this.getAllNotes()
        );
    }

}