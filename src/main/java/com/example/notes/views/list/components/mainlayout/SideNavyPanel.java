package com.example.notes.views.list.components.mainlayout;

import com.example.notes.data.Label;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.ArchivedNotes;
import com.example.notes.views.list.LabeledNotes;
import com.example.notes.views.list.NotesList;
import com.example.notes.views.list.TrashedNotes;
import com.example.notes.views.list.components.NotesGeneratorComp;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.example.notes.views.list.events.LabelsUpdateEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoIcon;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


public class SideNavyPanel extends Div {
    Integer numberOfNotesInTrash;
    Integer numberOfNotesInArchive;
    Integer numberOfNotes;
    NotesCounter countNotes;
    NotesCounter countArchivedNotes;
    NotesCounter countTrashedNotes;
    private final NoteService noteService;
    private SideNav labelsSideNav;
    private final LabelService labelService;
    public SideNavyPanel(NoteService noteService, LabelService labelService) {
        this.noteService = noteService;
        this.labelService = labelService;
        setNumberOfNotes();
        // todo Nema potrebe da imam tri podatka Integer, mislim može bez njih
        SideNav nav = new SideNav();
        SideNavItem notesLink = new SideNavItem("Notes", NotesList.class, VaadinIcon.NOTEBOOK.create());
        SideNavItem archivedNotesLink = new SideNavItem("Archive", ArchivedNotes.class, VaadinIcon.ARCHIVE.create());
        SideNavItem trashedNotesLink = new SideNavItem("Trash", TrashedNotes.class, VaadinIcon.TRASH.create());

        initializeNotesCounters();

        notesLink.setSuffixComponent(countNotes);
        archivedNotesLink.setSuffixComponent(countArchivedNotes);
        trashedNotesLink.setSuffixComponent(countTrashedNotes);

        nav.addItem(notesLink, archivedNotesLink, trashedNotesLink);
        nav.setCollapsible(true);
        nav.setLabel("Groups");


        Div navWrapper = new Div(nav);
        navWrapper.getStyle().set("margin","0.7vw");
        nav.setWidthFull();
        add(navWrapper);



        updateNotesNumbers();

        stylizeThisComponent();

        //todo napravi isti Editlabels dialog samo ga malo izmijeni
        //todo promijeni EditLabels pojednostavi
        //Odavde ide dio za dodavanje lablela u SideNav


        add(new NotesGeneratorComp(noteService));

        Button editLabelsButton = new Button("Edit Labels");
        HorizontalLayout editLablelsHl = new HorizontalLayout(editLabelsButton);

        editLablelsHl.setAlignItems(FlexComponent.Alignment.CENTER);
        labelsSideNav = new SideNav();
        updateSideNavLabels();
        Div navWrapper2 = new Div();
        navWrapper2.getStyle().set("margin","0.7vw");
        navWrapper2.add(editLablelsHl, labelsSideNav);
        add(navWrapper2);

        labelsSideNav.setCollapsible(true);
        labelsSideNav.setLabel("Labels");

        Dialog labelsDialog = getLabelsDialog();
        editLabelsButton.addClickListener(event -> labelsDialog.open());

    }
    private void stylizeThisComponent() {

    }

    private  Dialog getLabelsDialog() {
        Dialog labelsDialog = new Dialog("Labels dialog");
        Button closeDialogButton = new Button(LumoIcon.CROSS.create());
        closeDialogButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        labelsDialog.getHeader().add(closeDialogButton);
        closeDialogButton.addClickListener(event -> {
           labelsDialog.close();
        });

        Span message = new Span(" ");
        message.getStyle().setColor("red");
        message.getStyle().set("font-style","italic");
        message.setVisible(true);
        labelsDialog.add(message);

        VerticalLayout labelList = new VerticalLayout();
        HorizontalLayout addNewLabelRow = new HorizontalLayout();

        Button addLabelButton = new Button(new Icon("plus"));
        addLabelButton.setEnabled(false);


        TextField newLabelName = new TextField();
        addLabelButton.addClickListener(e -> {
            this.labelService.addLabel(newLabelName.getValue());
            ComponentUtil.fireEvent(UI.getCurrent(),new LabelsUpdateEvent(this,false));
            newLabelName.setValue("");
            labelList.removeAll();
            labelList.add(addNewLabelRow, getAllLabels());
            updateSideNavLabels();
        });

        newLabelName.setValueChangeMode(ValueChangeMode.EAGER);
        AtomicReference<ArrayList<String>> listLabelsName = new AtomicReference<>(new ArrayList<>(labelService.getAllLabels().stream().map(Label::getName).map(String::toUpperCase).toList()));
        ComponentUtil.addListener(UI.getCurrent(), LabelsUpdateEvent.class,event -> {
            listLabelsName.set(new ArrayList<>(labelService.getAllLabels().stream().map(Label::getName).map(String::toUpperCase).toList()));
        });
        newLabelName.addValueChangeListener(event -> {
            if(listLabelsName.get().contains(newLabelName.getValue().toUpperCase()) ) {
                addLabelButton.setEnabled(false);
                message.setText("Label already exists");
            }
            else if(newLabelName.getValue().isEmpty() || newLabelName.getValue().matches("\\s*")){
                addLabelButton.setEnabled(false);
                message.setText("Enter name");
            }
            else {
                addLabelButton.setEnabled(true);
                addLabelButton.setTooltipText("Add new label");
                message.setText("");
            }
        });

        addNewLabelRow.add(newLabelName, addLabelButton);
        labelList.add(addNewLabelRow, getAllLabels());
        labelsDialog.add(labelList);
        return labelsDialog;
    }

    private VerticalLayout getAllLabels() {
        VerticalLayout labelsList = new VerticalLayout();

        labelsList.getStyle().setPadding("0px 0px 0px 0px");
        this.labelService.getAllLabels().forEach(label -> {
            // Ime labele smještam u textfield
            HorizontalLayout labelRow = new HorizontalLayout();

            labelRow.getStyle().setMargin("0 1% 0 0");
            TextField labelName = new TextField();
            labelName.setValue(label.getName());
            Button deleteLabelButtonInDialog  = new Button(new Icon("close-circle"));
            deleteLabelButtonInDialog.setTooltipText("Delete label");
            deleteLabelButtonInDialog.addClickListener(click -> {
                this.labelService.deleteLabel(label);
                ComponentUtil.fireEvent(UI.getCurrent(),new LabelsUpdateEvent(this,false));
                labelRow.removeFromParent();
                updateSideNavLabels();
            });
            Button saveLabel = new Button(new Icon("check-circle-o"));
            saveLabel.setTooltipText("Sava name changes");
            saveLabel.addClickListener(click ->{
                label.setName(labelName.getValue());
                this.labelService.editLabelName(label.getLabelId(), labelName.getValue());
                ComponentUtil.fireEvent(UI.getCurrent(),new LabelsUpdateEvent(this,false));
                updateSideNavLabels();
            });
            // U svakom redu potrebni imati delete ikonicu, ime labele i trece dugme?
            labelRow.add(deleteLabelButtonInDialog, labelName, saveLabel);
            labelsList.add(labelRow);
            // Potrebno staviti u jedan red x za brisanje labelee

        });
        return labelsList;
    }

    private void updateSideNavLabels() {
        labelsSideNav.removeAll();
        this.labelService.getAllLabels().forEach(label -> {
            labelsSideNav.addItem(new SideNavItem(label.getName(),
                    LabeledNotes.class,
                    new RouteParameters("label",  label.getName())));
        });
    }

    private void updateNotesNumbers() {
        ComponentUtil.addListener(UI.getCurrent(), CountingNotesEvent.class, event -> {
            setNumberOfNotes();
            updateNumbersOfNotesCounters(countNotes, countArchivedNotes, countTrashedNotes);
        });
    }

    private void initializeNotesCounters() {
        countNotes = new NotesCounter(numberOfNotes.toString());
        countArchivedNotes = new NotesCounter(numberOfNotesInArchive.toString());
        countTrashedNotes = new NotesCounter(numberOfNotesInTrash.toString());
    }

    private void updateNumbersOfNotesCounters(NotesCounter countNotes, NotesCounter countArchivedNotes, NotesCounter countTrashedNotes) {
        countNotes.setText(numberOfNotes.toString());
        countArchivedNotes.setText(numberOfNotesInArchive.toString());
        countTrashedNotes.setText(numberOfNotesInTrash.toString());
    }

    private void setNumberOfNotes(){
        numberOfNotes = noteService.countNotes();
        numberOfNotesInArchive = noteService.countNotesInArchive();
        numberOfNotesInTrash = noteService.countNotesInTrash();
    }
}
