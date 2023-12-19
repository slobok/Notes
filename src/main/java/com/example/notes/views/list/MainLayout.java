package com.example.notes.views.list;

import com.example.notes.services.LabelService;

import com.example.notes.views.list.components.mainlayout.EditLabels;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;


public class MainLayout extends AppLayout {

    private Component labelsList;

    private final LabelService labelService;
    MainLayout(LabelService labelService){
        this.labelService = labelService;
        createHeader();
        createDrawer();
        this.updateDrawerLabelList();
    }

    private void updateDrawerLabelList(){
        this.remove(labelsList);
        this.labelsList = displayLabels();
        addToDrawer(labelsList);
    }
    private void createHeader() {
        // Making title
        H1 headerTitle = createHeaderTitle();
        DrawerToggle drawerToggle = createDrawerToggle();
        TextField search = createSearchNoteField();

        HorizontalLayout header = createHeaderLayout();
        header.add(drawerToggle, headerTitle);

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

    private static TextField createSearchNoteField() {
        TextField search = new TextField();
        search.setPlaceholder("Find note");
        search.getStyle().setMargin("auto 10%");
        search.setWidth("30%");
        return search;
    }

    public void createDrawer(){
        addToDrawer(
                fixedDrawerItems(),
                //editLabels(),
                new EditLabels(labelService)
        );
    }
    // U metodi ispod --
    // Ovo su stavke koje su fiksirane u draweru za razliku od liste labele koja je dinamična
    private VerticalLayout fixedDrawerItems() {
        RouterLink notesList = new RouterLink("Notes",NotesList.class);
        notesList.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink trashedNotes = new RouterLink("Trash", TrashedNotes.class);
        RouterLink archivedNotes = new RouterLink("Archive",ArchivedNotes.class);
        return new VerticalLayout(notesList, trashedNotes, archivedNotes);
    }
    // Slobodan: Todo make component for Router linkList

    private VerticalLayout displayLabels() {
        VerticalLayout listLabels = new VerticalLayout();
        this.labelService.getAllLabels().forEach(label -> {
            RouterLink link  = new RouterLink(label.getName(), LabeledNotes.class,
                    new RouteParameters("label",label.getName()));
            listLabels.add(link);
        });
        return listLabels;
    }

    // Prikaz DialogBox-a kada se klikne na dugme edit labels.
    // Kroz dialbox omogućeno dodavanje labela.

}
