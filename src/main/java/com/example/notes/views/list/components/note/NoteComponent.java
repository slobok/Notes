package com.example.notes.views.list.components.note;

import com.example.notes.data.Label;
import com.example.notes.data.Note;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.example.notes.views.list.events.LabelsUpdateEvent;
import com.example.notes.views.list.events.PinNoteEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataCommunicator;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
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
    private List<Label> allLabels;
    private VerticalLayout checkbox;
    private Component multiSelectLComboBox;
    private HorizontalLayout chooseColor;
    public NoteComponent(Note note, NoteService noteService, LabelService labelService){
        this.noteService = noteService;
        this.labelService = labelService;
        this.note = note;
        stylingThisComponent();
        this.noteMenu = createNoteMenu();
       // this.multiSelectLComboBox = makeLabelBox();
        this.chooseColor  = new HorizontalLayout(setNotesBackgroundColor());
        updateNote();
        setTextSaveMode();
        this.add(noteHeader, notesText ,noteMenu, chooseColor);
    }

    private Input setNotesBackgroundColor() {
        Input chooseColor = new Input();
        chooseColor.setType("color");
        chooseColor.setValue(note.getNoteColor());
        chooseColor.setValueChangeMode(ValueChangeMode.EAGER);
        chooseColor.getStyle().setCursor("pointer");
        chooseColor.addValueChangeListener(event -> {
            this.getStyle().setBackground(event.getValue());
            note.setNoteColor(event.getValue());
            this.noteService.saveNote(note);
        });
        return  chooseColor;
    }

    private void updateNote() {
        this.notesText = createNotesText();
        this.noteHeader = createNoteHeader();
    }

    //Methods of NoteComponents
    protected HorizontalLayout createNoteMenu() {
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.getStyle().setDisplay(Style.Display.BLOCK);
        addButtonsToNoteMenu(noteMenu);
        return noteMenu;
    }

    protected void addButtonsToNoteMenu(HorizontalLayout noteMenu) {
        noteMenu.add(
               // saveChangesButton(),
                getArchiveButton(),
                toTrashButton()
                );
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
        notesText.addValueChangeListener(event -> {
            note.setText(notesText.getValue());
            // TODO ne znam da li je ovaj save dobar mozda je bolje
            // TODO novu funckiju servisu da pravim koja samo ažurira
            // TODO tekst
            this.noteService.saveNote(note);
        });

        notesTitle.setValueChangeMode(ValueChangeMode.EAGER);
        notesTitle.addValueChangeListener(event -> {
            note.setTitle(notesTitle.getValue());
            this.noteService.saveNote(note);
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
        Button pin = getPinButton();
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

    protected void setEnabledNoteTitleAndText(){

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

    protected Button toTrashButton() {
        Button toTrashButton = new Button(new Icon("trash"));
        toTrashButton.setTooltipText("Move to trash");
        toTrashButton.addClickListener(click -> {
            toTrash();

            ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(this,false));
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
            this.noteService.togglePin(this.note.getNoteId());
            String message = this.note.isPinned() ? "Note unpinned" : "Note pinned";
            makeNotification(message,1200, Notification.Position.BOTTOM_START);
            ComponentUtil.fireEvent(UI.getCurrent(), new PinNoteEvent(pinButton,false));
        });
        return pinButton;
    }

    private Button getArchiveButton() {
        Button archiveButton = new Button(new Icon("archive"));
        archiveButton.setTooltipText("Archive note");
        archiveButton.addClickListener(click -> {
            toArchiveNote();
            ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(this,false));
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
            makeNotification("Successfully saved",1400, Notification.Position.MIDDLE);
        });
        return updateChangesButton;
    }

    private void toArchiveNote() {
        this.noteService.archiveNote(note.getNoteId());
        this.noteService.unpinNote(note.getNoteId());
    }

    private void toTrash() {
        this.noteService.moveToTrash(note);
        this.noteService.unpinNote(note.getNoteId());
    }

    private void updateNotesChanges(){
        note.setTitle(this.notesTitle.getValue());
        note.setText(this.notesText.getValue());
        this.noteService.saveNote(note);
        this.note = this.noteService.findById(note.getNoteId());
        updateNote();
        this.removeAll();
        this.add(noteHeader, notesText, noteMenu, chooseColor);
    }
    //Field for selecting labels
    private Component makeLabelBox(){
        MultiSelectComboBox<Label> labelMultiSelectComboBox = new MultiSelectComboBox<Label>("Labels");
        // Todo napravi ovu kompoentu kako bi mogao da koristis allLabels unutare eventa


        allLabels = new ArrayList<>(labelService.getAllLabels());
        labelMultiSelectComboBox.setItems(allLabels);
        labelMultiSelectComboBox.setItemLabelGenerator(Label::getName);

        ComponentUtil.addListener(UI.getCurrent(), LabelsUpdateEvent.class,(ComponentEventListener<LabelsUpdateEvent>) event -> {
            allLabels = new ArrayList<>(labelService.getAllLabels());
            labelMultiSelectComboBox.setItems(allLabels);
            labelMultiSelectComboBox.setItemLabelGenerator(Label::getName);

            List<Long> selectedLabels = noteService.getNoteLabels(note.getNoteId()).stream().map(
                    Label::getLabelId
            ).toList();
            System.out.println("Selected labels +  " + selectedLabels);
            allLabels.forEach(label -> {
                if(selectedLabels.contains(label.getLabelId())){
                    System.out.println("usao u petlju selected " + label);
                    labelMultiSelectComboBox.select(label);

                }
            });
        });


        List<Long> selectedLabels =  noteService.getNoteLabels(note.getNoteId()).stream().map(
                Label::getLabelId
        ).toList();
        System.out.println(selectedLabels);
        allLabels.forEach(label -> {
            if(selectedLabels.contains(label.getLabelId())){
                labelMultiSelectComboBox.select(label);
            }
            labelMultiSelectComboBox.addSelectionListener(event -> {
                if(!event.getAddedSelection().isEmpty()){
                    ArrayList<Label> addedLabel = new ArrayList<>(event.getAddedSelection());
                    this.noteService.addLabelsToNote(note, addedLabel);
                }
                else if(!event.getRemovedSelection().isEmpty()) {
                    ArrayList<Label> labelList = new ArrayList<>(event.getRemovedSelection());
                    this.noteService.removeLabelSFromNote(note, labelList);
                }
            });
        });

        return labelMultiSelectComboBox;
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