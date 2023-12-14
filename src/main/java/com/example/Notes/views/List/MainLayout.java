package com.example.Notes.views.List;

import com.example.Notes.Services.LabelService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
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
        getHeader();
        createDrawer();
        this.updateDrawerLabelList();
    }

    private void updateDrawerLabelList(){
        this.remove(labelsList);
        this.labelsList = displayLabels();
        addToDrawer(labelsList);
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
                trashedNotes,
                editLabels()
        ));
    }


    private Component displayLabels() {
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
    private Button editLabels(){
        Button editLabels = new Button("Edit labels");

        Dialog dialogLabels = new Dialog();
        dialogLabels.setHeaderTitle("Edit labels:");

        editLabels.addClickListener(e  -> {
            dialogLabels.open();
                }
        );

        VerticalLayout labelList = new VerticalLayout();

        HorizontalLayout addNewLabel = new HorizontalLayout();
        addNewLabel.getStyle().setBorder("1px solid black");
        addNewLabel.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);

        Button addLabelButton = new Button(new Icon("plus"));
        addLabelButton.setTooltipText("Click to add label");

        TextField newLabel = new TextField();
        newLabel.setLabel("New label");

        addLabelButton.addClickListener(e -> {
            labelService.addLabel(newLabel.getValue());

            newLabel.setValue("");
            labelList.removeAll();
            labelList.add(addNewLabel, getAllLabels());
            this.updateDrawerLabelList();
        });
        addNewLabel.add(newLabel, addLabelButton);
        labelList.add(addNewLabel, getAllLabels());

        dialogLabels.add(labelList);

        return  editLabels;
    }
    // Funkcija koja prikazije sve labele U vertiacal layout formi(jedna ispod druge).
    private VerticalLayout getAllLabels(){
        VerticalLayout labelsList = new VerticalLayout();
        this.labelService.getAllLabels().forEach(label -> {
            TextField labelName = new TextField();
            labelName.setValue(label.getName());
            labelsList.add(labelName);
        });
        return labelsList;
    }
}
