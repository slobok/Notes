package com.example.notes.views.list;

import com.example.notes.data.Note;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.mainlayout.SideNavyPanel;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.example.notes.views.list.events.PinNoteEvent;
import com.example.notes.views.list.events.SearchNoteEvent;
import com.example.notes.views.list.events.SelectNoteEvent;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.HashSet;
import java.util.Set;

public class MainLayout extends AppLayout {

    private final LabelService labelService;
    private final NoteService noteService;
    private HorizontalLayout header;
    Span numberOfNotes = new Span();
    Div selectedNotesMenu;
    Set<Long> selectedNotes = new HashSet<>();
    MainLayout(LabelService labelService,
               NoteService noteService) {
        this.labelService = labelService;
        this.noteService = noteService;
        createSelectedNotesMenu();
        createHeader();
        createDrawer();
        addListeners();

        ComponentUtil.addListener(UI.getCurrent(),SelectNoteEvent.class, (ComponentEventListener<SelectNoteEvent>) event -> {
            addOrRemoveNote(event.getNoteId());
            numberOfNotes.setText("Selected " + selectedNotes.size() + " ");
            selectedNotesMenu.setVisible(!selectedNotes.isEmpty());
        });

    }

    private void createSelectedNotesMenu() {
        selectedNotesMenu = new Div();
        selectedNotesMenu.setVisible(false);
        Button buttonPin = new Button(VaadinIcon.PIN.create());
        buttonPin.addClickListener(event -> {
            this.noteService.pinManyNotes(selectedNotes);
            updateSelectedNotesmenu();
            ComponentUtil.fireEvent(UI.getCurrent(),new PinNoteEvent(new Button(),false));
        });

        Button archiveButton = new Button(VaadinIcon.ARCHIVE.create());
        archiveButton.addClickListener(event -> {
            this.noteService.archiveMoreNotes(selectedNotes);
            updateSelectedNotesmenu();
            ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(this, false));
        });
        selectedNotesMenu.add(numberOfNotes, buttonPin, archiveButton);
    }

    private void  updateSelectedNotesmenu(){
        selectedNotes.clear();
        selectedNotesMenu.setVisible(false);
    }

    private void addOrRemoveNote(Long noteId) {
        if (selectedNotes.contains(noteId)){
            selectedNotes.remove(noteId);
        }
        else{
            selectedNotes.add(noteId);
        }
    }

    private void createHeader() {
        H1 headerTitle = createHeaderTitle();
        DrawerToggle drawerToggle = createDrawerToggle();
        header = createHeaderLayout();
        TextField searchField = createSearchNoteField();

        header.add(drawerToggle, headerTitle, searchField, selectedNotesMenu);
        addToNavbar(header);
    }

    private static DrawerToggle createDrawerToggle() {
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.getStyle().setBorder("2px solid black");
        return drawerToggle;
    }

    private static H1 createHeaderTitle() {
        return new H1("Notes");
    }

    private static HorizontalLayout createHeaderLayout() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return header;
    }

    private  TextField createSearchNoteField() {
        TextField search = new TextField();
        search.setValue("");
        search.setPrefixComponent(new Icon("lumo", "search"));
        search.setPlaceholder("Find note");
        search.getStyle().setMargin("auto 10%");
        search.setWidth("30%");
        search.setValueChangeMode(ValueChangeMode.LAZY);
        search.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
                // Fire an event on the event bus
                ComponentUtil.fireEvent(UI.getCurrent() , new SearchNoteEvent(event.getSource(), false, event.getValue()));
            }
        });
        return search;
    }

    public void createDrawer(){
        addToDrawer(
                //    new DrawerMenuIList(noteService),
                new SideNavyPanel(noteService, labelService)
                //  new EditLabels(labelService)
        );
    }

    protected void addListeners() {
        listenerForPinNoteEvent();
        listenerToForAddingNewNote();
    }



    private void listenerForPinNoteEvent() {
        ComponentUtil.addListener(UI.getCurrent(), PinNoteEvent.class, (ComponentEventListener<PinNoteEvent>) event ->  {
            updateSelectedNotesmenu();
        });
    }

    private void listenerToForAddingNewNote() {
        ComponentUtil.addListener(UI.getCurrent(), CountingNotesEvent.class, (ComponentEventListener<CountingNotesEvent>) event ->  {
            updateSelectedNotesmenu();
        });
    }

}