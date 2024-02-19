package com.example.notes.views.list.components.note;

import com.example.notes.data.Label;
import com.example.notes.data.Note;
import com.example.notes.views.list.components.note.NoteEvents.NoteClickListeners;
import com.example.notes.views.list.components.note.NoteEvents.NotesComponents;
import com.example.notes.views.list.events.SelectNoteEvent;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class NoteComponent extends VerticalLayout {

    protected Note note;
    private HorizontalLayout noteHeader;
    private TextField notesTitle;
    private TextArea notesText;
    private HorizontalLayout noteMenu;
    private List<Label> allLabels;
    private VerticalLayout checkbox;
    private Component multiSelectLComboBox;
    private HorizontalLayout chooseColor;
    protected boolean selected = false;
    protected final NotesComponents noteComponents;
    protected final NoteClickListeners noteClickListeners;
    public NoteComponent(Note note, NotesComponents noteComponents, NoteClickListeners noteClickListeners){
        this.note = note;
        this.noteComponents = noteComponents;
        this.noteClickListeners = noteClickListeners;
        stylingThisComponent();
        this.noteMenu = createNoteMenu();
        // this.multiSelectLComboBox = makeLabelBox();
        this.chooseColor  = new HorizontalLayout(setNotesBackgroundColor());
        updateNote();
        setTextSaveMode();
        addComponentsToNote();
        addButtons();
        this.addDoubleClickListener(event -> {
            selectUnselect();
        });
    }

    private void addComponentsToNote() {
        this.add(noteHeader, notesText ,noteMenu, chooseColor, fileUpload(), downlaodLinksForFile());
    }

    // todo popravi funkciju
    private void selectUnselect() {
        ComponentUtil.fireEvent(UI.getCurrent(),new SelectNoteEvent(this, false, note.getNoteId()));
        selected = !selected;
        if(selected){
            this.getStyle().setBorder("4px solid black");
        }
        else {
            this.getStyle().setBorder("4px solid transparent");
        }
    }

    private Input setNotesBackgroundColor() {
        Input chooseColor = new Input();
        chooseColor.setType("color");
        chooseColor.setValue(note.getNoteColor());
        chooseColor.setValueChangeMode(ValueChangeMode.EAGER);
        chooseColor.getStyle().setCursor("pointer");
        chooseColor.addValueChangeListener(noteClickListeners.getValueChangeEventValueChangeListener(this, note));
        return  chooseColor;
    }

    private void updateNote() {
        this.notesText = createNotesText();
        this.noteHeader = createNoteHeader();
    }

    protected HorizontalLayout createNoteMenu() {
        noteMenu = new NoteMenu(noteClickListeners, note);
        return noteMenu;
    }

    protected void addButtonsToNoteMenu(List<Button> buttonList) {
        buttonList.forEach(button ->  noteMenu.add(button));
    }

    protected void addButtons(){
        addButtonsToNoteMenu(new ArrayList<>(List.of(toTrashButton(), toArchiveButton(), uploadALotOfFilesButton())));
    }

    private  TextArea createNotesText() {
        TextArea notesText = new TextArea();
        notesText.setValue(note.getText());
        notesText.getStyle().setMargin("0");
        notesText.getStyle().setDisplay(Style.Display.BLOCK);
        notesText.setVisible(true);
        return notesText;
    }

    private void setTextSaveMode() {
        notesText.setValueChangeMode(ValueChangeMode.EAGER);
        notesText.addValueChangeListener(event -> noteClickListeners.saveNotesText(note, notesText.getValue()));
        notesTitle.setValueChangeMode(ValueChangeMode.EAGER);
        notesTitle.addValueChangeListener(event -> {
            noteClickListeners.saveNotesTitle(note, notesTitle.getValue());
        });
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
        // noteHeader.getStyle().setBorder("1px solid black");
        noteHeader.getStyle().setMargin("0px 0px 0px 0px");
        noteHeader.getStyle().setPadding("0px 0px 0px 0px");
        noteHeader.setVerticalComponentAlignment(Alignment.CENTER);
        Button pin = pinButton();
        pin.getStyle().setMargin("0px 0px 0px auto");
        notesTitle = new TextField();
        notesTitle.setMaxLength(50);
        notesTitle.setValue(note.getTitle());
        notesTitle.getStyle().setMargin("0");
        notesTitle.getStyle().setPadding("0px");
        notesTitle.getStyle().setFont("88px");
        notesTitle.getStyle().setFont("Google Sans,Arial,Roboto");

        noteHeader.add(notesTitle, pin);
        return noteHeader;
    }

    private void stylingThisComponent() {
        this.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.getStyle().setBoxShadow("2px 2px 2px 2px linen");
        this.getStyle().set("border-radius","1rem");
        this.setMargin(true);
        this.setWidth("23rem");
        this.setMinWidth("310px");
        this.getStyle().setBackground(note.getNoteColor());
    }

    protected Button pinButton() {
        Icon pinIcon = new Icon("pin");
        String pinIconColor = this.note.isPinned() ? "black" : "gray";
        pinIcon.setColor(pinIconColor);
        Button pinButton = new Button(pinIcon);
        pinButton.getStyle().setFloat(Style.FloatCss.RIGHT);
        String tooltipText = this.note.isPinned() ? "Unpin" : "Pin";
        pinButton.setTooltipText(tooltipText);
        pinButton.addClickListener(e ->
                this.noteClickListeners.pinButtonClick(note)
        );
        return pinButton;
    }

    protected Button saveChangesButton() {
        Button updateChangesButton = new Button("Save");
        updateChangesButton.setTooltipText("Save note changes");
        updateChangesButton.addClickListener(click -> {
            updateNotesChanges();
            makeNotification("Successfully saved",1400, Notification.Position.MIDDLE);
        });
        return updateChangesButton;
    }

    private void updateNotesChanges(){
        note.setTitle(this.notesTitle.getValue());
        note.setText(this.notesText.getValue());
        note = this.noteClickListeners.saveNoteToDnAndGet(note);
        // Ne bi trebalo da mora
        updateNote();
        this.removeAll();
        addComponentsToNote();
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

    private Component downlaodLinksForFile() {
        return noteComponents.downloadLinkForFiles(note);
    }

    HorizontalLayout fileUpload(){
        return noteComponents.fileUploadWithMultiFileBuffer(note);
    }
    public Button toArchiveButton() {
        Button archiveButton = new Button(new Icon("archive"));
        archiveButton.setTooltipText("Archive note");
        archiveButton.addClickListener(click ->
                noteClickListeners.archiveButtonClickListener(note)
        );
        return archiveButton;
    }

    public Button toTrashButton() {
        Button toTrashButton = new Button(new Icon("trash"));
        toTrashButton.setTooltipText("Move to trash");
        toTrashButton.addClickListener(event -> {
            noteClickListeners.noteMoveToTrash(note);
        });
        return toTrashButton;
    }

    protected Button uploadALotOfFilesButton(){
        Button upload1000Files = new Button(VaadinIcon.FILE_O.create());
        upload1000Files.addClickListener(event -> {
            try {
                noteClickListeners.generateHugeNumberOfFiles(note);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return upload1000Files;
    }
}