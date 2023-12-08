package com.example.Notes.views.List;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {
    MainLayout(){
        getHeader();
        createDrawer();
    }

    private void getHeader() {
        // Making title
        H1 h1 = new H1("Notes");
        // h1.getStyle().setBorder("1px solid black");

        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.getStyle().setBorder("2px solid black");

        TextField search = new TextField();
        search.setPlaceholder("Find note");
        search.getStyle().setMargin("auto 10%");
        search.setWidth("30%");

        HorizontalLayout header = new HorizontalLayout();
        //  header.getStyle().setBorder("2px solid red");
        header.setWidthFull();
        header.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        // Search izbacen zasad
        header.add(drawerToggle, h1);

        addToNavbar(header);
    }
    public void createDrawer(){
        RouterLink notesList = new RouterLink("Notes",NotesList.class);
        notesList.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink trashedNotes = new RouterLink("Trash", TrashedNotes.class);
        RouterLink archivedNotes = new RouterLink("Archive",ArchivedNotes.class);
        addToDrawer(new VerticalLayout(
                notesList,
                archivedNotes,
                trashedNotes
        ));
    }

}
