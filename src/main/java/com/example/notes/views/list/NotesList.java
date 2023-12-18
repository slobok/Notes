package com.example.notes.views.list;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.NotesContainer;
import com.example.notes.views.list.components.note.NoteComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class NotesList extends VerticalLayout {
    protected NoteService noteService;
    private TextField search = new TextField();
    NotesList(NoteService noteService) {
        this.noteService = noteService;
        addToConstructor();
    }

    public TextField getSearch() {
        return search;
    }

    protected void addToConstructor() {
        this.add(
                getSearchField(),
                createNoteField(),
                getAllNotes()
        );
    }

    protected Component getSearchField() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidthFull();
        this.search.setPlaceholder("Search");
        this.search.setPrefixComponent(new Icon("lumo", "search"));
        search.addValueChangeListener(e -> {
            this.updatePage();
        });
        hl.add(search);
        return hl;
    }

    private Component createNoteField() {
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
        // Kontejner u kome skladištim sve komponent all notes
        VerticalLayout allNotes = new VerticalLayout();
        // Komponentu dvije grupe podijeljene pinned u unpinnded.
        // NotesContainer komponenta naparvljena u te svrhe
        NotesContainer notesContainer = new NotesContainer(new H6("Others"));
        notesContainer.getTitle().setVisible(false);
        NotesContainer pinnedNotes = new NotesContainer(new H6("Pinned"));
        pinnedNotes.setVisible(false);

        this.noteService.getAllNotes(this.search.getValue())
                .forEach(n -> {
                    NoteComponent noteComponent = new NoteComponent(n, noteService);
                            // notes.add(NoteComponent);
                            //TODO https://vaadin.com/docs/latest/create-ui/creating-components

                    if (n.isPinned()) {
                        pinnedNotes.setVisible(true);
                        pinnedNotes.add(noteComponent);
                            }
                    else {
                        if(pinnedNotes.isVisible()){
                            notesContainer.getTitle().setVisible(true);
                        }
                        notesContainer.add(noteComponent);
                        }
                    allNotes.add(pinnedNotes, notesContainer);
                        }
                );
        return allNotes;
    }

    protected void updatePage() {
        this.removeAll();
        this.add(
                this.getSearchField(),
                this.createNoteField(),
                this.getAllNotes()
        );
    }
}