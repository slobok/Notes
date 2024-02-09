package com.example.notes.views.list;
import com.example.notes.data.Note;
import com.example.notes.repository.FileContentDbRepository;
import com.example.notes.services.FajlService;
import com.example.notes.services.FileContentService;
import com.example.notes.services.Helper.LobHelper;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.NewNoteForm;
import com.example.notes.views.list.components.NotesContainer;
import com.example.notes.views.list.components.PaginationComp;
import com.example.notes.views.list.components.note.NoteComponent;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.example.notes.views.list.events.PinNoteEvent;
import com.example.notes.views.list.events.SearchNoteEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Route(value = "", layout = MainLayout.class)
public class NotesList extends VerticalLayout  {
    protected NoteService noteService;
    protected LabelService labelService;
    protected FajlService fajlService;
    protected LobHelper lobHelper;
    private TextField search = new TextField();
    protected H3 message;
    NotesContainer notesContainer;
    NotesContainer pinnedNotesLayout;
    ListDataProvider<List<Note>> otherListDataProvider;
    ListDataProvider<List<Note>> pinListDataProvider;
    List<List<Note>> listOfPinnedNotesList;
    List<List<Note>> listOfOtherNotesList;
    List<Note> allNotes;
    List<Note> selectedNotes = new LinkedList<>();
    protected final SessionFactory sessionFactory;
    protected final FileContentService fileContentService;
    NotesList(NoteService noteService, LabelService labelService, FajlService fajlService, SessionFactory sessionFactory, FileContentService fileContentService) {
        this.labelService = labelService;
        this.noteService = noteService;
        this.fajlService = fajlService;
        this.sessionFactory = sessionFactory;
        this.fileContentService = fileContentService;
        createMessage();
        addComponentsToPage();
        addListeners();
    }

    protected void addListeners() {
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
                new NewNoteForm(noteService),
                getAllNotes(),
                message
                ,new PaginationComp(10,201)
                // new PaginationComp(8,noteService.countNotes())
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


    protected VerticalLayout getAllNotes() {
        // Two container should be inside one for pinned, one for other notes
        VerticalLayout allNotesLayout = new VerticalLayout();

        notesContainer = new NotesContainer(new H6("Others"));
        notesContainer.getTitle().setVisible(false);
        notesContainer.setPadding(false);
        notesContainer.setMargin(false);

        pinnedNotesLayout = new NotesContainer(new H6("Pinned"));
        pinnedNotesLayout.setVisible(false);
        pinnedNotesLayout.getTitle().setVisible(true);
        pinnedNotesLayout.setMargin(false);
        pinnedNotesLayout.setPadding(false);

        allNotes = this.noteService.getAllNotes(search.getValue());
        if(allNotes.isEmpty()){
            message.setVisible(true);
        }

        List<Note> pinnedNotes = allNotes.stream().filter(Note::isPinned).toList();
        if(!pinnedNotes.isEmpty()){
            pinnedNotesLayout.setVisible(true);
        }

        List<Note> otherNotes = allNotes.stream().filter(note -> !note.isPinned()).toList();
        if(!otherNotes.isEmpty() && !pinnedNotes.isEmpty()){
            notesContainer.getTitle().setVisible(true);
        }

        listOfPinnedNotesList = makeListsOfList(pinnedNotes);
        listOfOtherNotesList = makeListsOfList(otherNotes);
        otherListDataProvider = new ListDataProvider<>(listOfOtherNotesList);
        pinListDataProvider = new ListDataProvider<>(listOfPinnedNotesList);
        // Making two virtual lists one for pinnedNotes, second for others

        VirtualList<List<Note>> pinnedNotesVirtualList = new VirtualList<>();
        pinnedNotesVirtualList.setWidthFull();

        VirtualList<List<Note>> otherNotesVirtualList = new VirtualList<>();
        pinnedNotesVirtualList.setWidthFull();

        //Making ComponentRenderer - Three notes set in one div. Reason is to be full width List

        ComponentRenderer<Component, List<Note>> noteRenderer = makeNotesRenderer();

        //Setting 2 virutalLists
        pinnedNotesVirtualList.setRenderer(noteRenderer);
        otherNotesVirtualList.setRenderer(noteRenderer);

        pinnedNotesVirtualList.setDataProvider(pinListDataProvider);
        otherNotesVirtualList.setDataProvider(otherListDataProvider);

        pinnedNotesLayout.add(pinnedNotesVirtualList);
        notesContainer.add(otherNotesVirtualList);

        allNotesLayout.add(pinnedNotesLayout ,notesContainer);
        allNotesLayout.setPadding(false);
        allNotesLayout.setMargin(false);
        return allNotesLayout;
    }

    protected ComponentRenderer<Component, List<Note>> makeNotesRenderer() {
        return new ComponentRenderer<>(notes  -> {
            Div div = new Div();
            div.setWidth("100%");
            div.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
            notes.forEach(note -> {
                setNoteType(note, div);
            });
            return div;
        }
        );
    }

    protected void setNoteType(Note note, Div div) {
        div.add(new NoteComponent(note, noteService, labelService, fajlService, sessionFactory, fileContentService));
    }

    protected static List<List<Note>> makeListsOfList(List<Note> pinnedNotes) {
        List<List<Note>> resultList = new ArrayList<>();
        List<Note> noteList4 = new ArrayList<>();

        for (int index = 1; index <= pinnedNotes.size(); index++) {
            noteList4.add(pinnedNotes.get(index - 1));
            if(index != 0 && index % 3 == 0){
                resultList.add(noteList4);
                noteList4 = new ArrayList<>();
            }
        }
        resultList.add(noteList4);
        return resultList;
    }

    protected void updatePage() {
        allNotes = this.noteService.getAllNotes(search.getValue());

        message.setVisible(allNotes.isEmpty());

        List<Note> pinnedNotes = allNotes.stream().filter(Note::isPinned).toList();

        pinnedNotesLayout.setVisible(!pinnedNotes.isEmpty());

        List<Note> otherNotes = allNotes.stream().filter(note -> !note.isPinned()).toList();
        notesContainer.getTitle().setVisible(!pinnedNotes.isEmpty() && !otherNotes.isEmpty());

        listOfPinnedNotesList = makeListsOfList(pinnedNotes);
        listOfOtherNotesList = makeListsOfList(otherNotes);

        pinListDataProvider.getItems().clear();
        pinListDataProvider.getItems().addAll(listOfPinnedNotesList);
        pinListDataProvider.refreshAll();

        otherListDataProvider.getItems().clear();
        otherListDataProvider.getItems().addAll(listOfOtherNotesList);
        otherListDataProvider.refreshAll();
    }
}