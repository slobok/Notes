package com.example.notes.views.list;

import com.example.notes.services.LabelService;

import com.example.notes.views.list.components.mainlayout.EditLabels;
import com.example.notes.views.list.components.mainlayout.drawer.DrawerMenuIList;
import com.example.notes.views.list.events.SearchNoteEvent;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class MainLayout extends AppLayout {

    private Component labelsList;

    private final LabelService labelService;
    MainLayout(LabelService labelService){
        this.labelService = labelService;
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
         h1.getStyle().setBorder("1px solid black");
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
    private static TextField createSearchNoteField() {
        TextField search = new TextField();
        search.setPlaceholder("Find note");
        search.getStyle().setMargin("auto 10%");
        search.setWidth("30%");
        search.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
                ComponentUtil.fireEvent(search, new SearchNoteEvent(event.getSource(), event.getValue()));
            }
        });
        return search;
    }

    public void createDrawer(){
        addToDrawer(
                new DrawerMenuIList(),
                new EditLabels(labelService)
        );
    }
}