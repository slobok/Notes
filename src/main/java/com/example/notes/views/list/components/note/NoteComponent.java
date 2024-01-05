package com.example.notes.views.list.components.note;

import com.example.notes.data.Label;
import com.example.notes.data.Note;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.example.notes.views.list.events.PinNoteEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;


import java.util.*;

public class NoteComponent extends VerticalLayout {

    protected final NoteService noteService;
    protected final LabelService labelService;
    protected Note note;
    private HorizontalLayout noteHeader;
    private TextField notesTitle;
    private TextArea notesText;
    private HorizontalLayout noteMenu;
    private VerticalLayout checkbox;
    private VerticalLayout multiSelectLComboBox;

    public NoteComponent(Note note, NoteService noteService, LabelService labelService){
        this.noteService = noteService;
        this.labelService = labelService;
        this.note = note;
        this.multiSelectLComboBox = makeLabelBox();
        stylingThisComponent();
        this.noteMenu = createNoteMenu();
        updateNote();
        this.add(noteHeader, notesText, multiSelectLComboBox ,noteMenu);
    }

    private void updateNote() {
        this.notesText = createNotesText();
        this.noteHeader = createNoteHeader();
    }

    //Methods of NoteComponents
    protected HorizontalLayout createNoteMenu() {
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        noteMenu.add(
                saveChangesButton(),
                getArchiveButton(),
                toTrashButton()
                );
        return noteMenu;
    }

    private  TextArea createNotesText() {
        TextArea notesText = new TextArea();
        notesText.setValue(note.getText());
        notesText.getStyle().setMargin("0");
        notesText.getStyle().setDisplay(Style.Display.BLOCK);
        notesText.setVisible(true);
        notesText.getChildren().forEach(el -> {
            el.getStyle().setBackground("red");
        });
        return notesText;
    }
    private VerticalLayout createCheckBox() {
        VerticalLayout checkbox = new VerticalLayout();
        checkbox.getStyle().setPadding("0px");
        checkbox.getStyle().setMargin("0px");
        List<String> wordsList = new ArrayList<>(List.of(this.note.getText().split("\\n")));
        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
        checkboxGroup.setItems(wordsList);
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        checkbox.add(checkboxGroup);
        return checkbox;
    }

    private HorizontalLayout createNoteHeader() {
        HorizontalLayout noteHeader =  new HorizontalLayout();
        noteHeader.getStyle().setMargin("0px 0px 0px 0px");
        noteHeader.getStyle().setPadding("0px 0px 0px 0px");
        noteHeader.setVerticalComponentAlignment(Alignment.CENTER);
        Button pin = getPinButton();
        pin.getStyle().setFloat(Style.FloatCss.RIGHT);
        notesTitle = new TextField();
        notesTitle.setValue(note.getTitle());
        notesTitle.getStyle().setMargin("0");
        notesTitle.getStyle().setPadding("0px");
        notesTitle.getStyle().setFont("88px");
        noteHeader.add(notesTitle, pin);
        return noteHeader;
    }

    private void stylingThisComponent() {
        this.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.getStyle().setBoxShadow("2px 2px 2px 2px linen");
        this.getStyle().set("border-radius","1rem");
        this.setMargin(true);
        this.getStyle().setBackground("#FFFAF0");
        this.setWidth("27rem");
    }

    protected Button toTrashButton() {
        Button toTrashButton = new Button(new Icon("trash"));
        toTrashButton.setTooltipText("Move to trash");
        toTrashButton.addClickListener(click -> {
            toTrash();
            // ispali event
            ComponentUtil.fireEvent(UI.getCurrent(),new CountingNotesEvent(this,false));
            makeNotification(
                    "Note moved to Trash",
                    1000,
                     Notification.Position.BOTTOM_START);
            this.removeFromParent();
        });
        return toTrashButton;
    }

    protected Button getPinButton() {
        Icon pinIcon = new Icon("pin");
        String pinIconColor = this.note.isPinned() ? "black" : "gray";
        pinIcon.setColor(pinIconColor);
        Button pinButton = new Button(pinIcon);
        pinButton.getStyle().setFloat(Style.FloatCss.RIGHT);
        String tooltipText = this.note.isPinned() ? "Unpin" : "Pin";
        pinButton.setTooltipText(tooltipText);
        pinButton.addClickListener(e -> {
            this.noteService.togglePin(this.note.getId());
            String message = this.note.isPinned() ? "Note unpinned" : "Note pinned";
            makeNotification(message,1200, Notification.Position.BOTTOM_START);
            ComponentUtil.fireEvent(UI.getCurrent(),new PinNoteEvent(pinButton,false));
        });
        return pinButton;
    }

    private Button getArchiveButton() {
        Button archiveButton = new Button(new Icon("archive"));
        archiveButton.setTooltipText("Archive note");
        archiveButton.addClickListener(click -> {
            toArchiveNote();
            ComponentUtil.fireEvent(UI.getCurrent(),new CountingNotesEvent(this,false));
            // this.updatePage();
            String message = note.isPinned() ? "Note archived and unpinned" : "Note archived";
            makeNotification(
                    message,
                    1000,
                    Notification.Position.BOTTOM_START
            );
           this.removeFromParent();
        });
        return archiveButton;
    }

    protected Button saveChangesButton() {
        Button updateChangesButton = new Button("Save");
        updateChangesButton.setTooltipText("Save note changes");
        updateChangesButton.addClickListener(click -> {
            updateNotesChanges();
            makeNotification("Succesfully saved",1400, Notification.Position.MIDDLE);
        });
        return updateChangesButton;
    }

    private void toArchiveNote() {
        this.noteService.archiveNote(note.getId());
        this.noteService.unpinNote(note.getId());
    }

    private void toTrash() {
        this.noteService.moveToTrash(note);
        this.noteService.unpinNote(note.getId());
    }

    private void updateNotesChanges(){
        note.setTitle(this.notesTitle.getValue());
        note.setText(this.notesText.getValue());
        this.noteService.saveNote(note);
        this.note = this.noteService.findById(note.getId());
        updateNote();
        this.removeAll();
        this.add(noteHeader, notesText, multiSelectLComboBox, noteMenu);
    }
    //Field for selecting labels
    //Using component Vaadin Component MultiSelectLComboBox
    private VerticalLayout makeLabelBox(){
        MultiSelectComboBox<Label> labelMultiSelectComboBox = new MultiSelectComboBox<Label>("Labels");
        labelMultiSelectComboBox.setItems(labelService.getAllLabels());
        labelMultiSelectComboBox.setItemLabelGenerator(Label::getName);
        labelMultiSelectComboBox.addSelectionListener(event -> {
            //Sta je bolje? Uvijek sve labele šalji ka serveru ili samo izmjenu
            //Mnogo toga može da se desi da se šalje
            this.noteService.addLabelsToNote(note, event.getAddedSelection());
                    System.out.println(event.getAddedSelection());

            ArrayList <Label> newLabelsList =  new ArrayList<Label>(event.getValue().stream().toList());
            Label addedLabel = newLabelsList.get(newLabelsList.size() - 1);
            ArrayList <Label> oldLabelsList = new ArrayList<>(event.getOldValue().stream().toList());

            if (event.getOldValue().size() < event.getValue().size()){
               // this.noteService.addLabelToNote(note, addedLabel);
            }
            else {
           //     this.noteService.removeLabelFromNote(note, addedLabel);
            }
        }

        );
        return new VerticalLayout(labelMultiSelectComboBox);
    }


    protected static MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();
        MenuItem moreItem = menuBar.addItem(new Icon("ellipsis-v"));
        SubMenu moreItemSubMenu = moreItem.getSubMenu();
        moreItemSubMenu.addItem("To trash");
        return menuBar;
    }

    private static Button getShowHideCheckBoxes() {
        Button showHideCheckBoxes = new Button("Show checkboxes");
        showHideCheckBoxes.setEnabled(false);
        showHideCheckBoxes.addClickListener(click -> {
            //this.noteService.setIsInChechBoxSyle(n);
        });
        return showHideCheckBoxes;
    }

    protected void makeNotification(String notificationText,int durationInMilliseconds,Notification.Position position ){
        Notification notification =  Notification.show(notificationText);
        notification.setDuration(durationInMilliseconds);
        notification.setPosition(position);
    }

}