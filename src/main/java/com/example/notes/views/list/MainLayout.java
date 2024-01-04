package com.example.notes.views.list;

import com.example.notes.services.LabelService;

import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.mainlayout.EditLabels;
import com.example.notes.views.list.components.mainlayout.drawer.DrawerMenuIList;
import com.example.notes.views.list.events.SearchNoteEvent;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class MainLayout extends AppLayout {

    private Component labelsList;
    private final LabelService labelService;
    private final NoteService noteService;
    MainLayout(LabelService labelService,
               NoteService noteService) {
        this.labelService = labelService;
        this.noteService = noteService;

        createHeader();
        createDrawer();
    }

    private void createHeader() {
        // Making title
        H1 headerTitle = createHeaderTitle();
        DrawerToggle drawerToggle = createDrawerToggle();
        HorizontalLayout header = createHeaderLayout();
        TextField searchField = createSearchNoteField();
        header.add(drawerToggle, headerTitle, searchField);
        addToNavbar(header);
    }

    private static DrawerToggle createDrawerToggle() {
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.getStyle().setBorder("2px solid black");
        return drawerToggle;
    }

    private static H1 createHeaderTitle() {
        H1 h1 = new H1("Notes");
       //  h1.getStyle().setBorder("1px solid black");
        return h1;
    }

    private static HorizontalLayout createHeaderLayout() {
        HorizontalLayout header = new HorizontalLayout();
        //  header.getStyle().setBorder("2px solid red");
        header.setWidthFull();
        header.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return header;
    }
    // Osposobiti naknadno search i ubaciti

    private  TextField createSearchNoteField() {
        TextField search = new TextField();
        search.setValue("");
        search.setPrefixComponent(new Icon("lumo", "search"));
        search.setPlaceholder("Find note");
        search.getStyle().setMargin("auto 10%");
        search.setWidth("30%");

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
                new DrawerMenuIList(noteService),
                new EditLabels(labelService)
        );
    }
}