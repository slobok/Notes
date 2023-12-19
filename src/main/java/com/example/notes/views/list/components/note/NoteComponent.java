package com.example.notes.views.list.components.note;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import java.util.ArrayList;
import java.util.List;

public class NoteComponent extends VerticalLayout {
    protected Note note;
    protected final NoteService noteService;
    private HorizontalLayout noteHeader;
    private TextField notesTitle;
    private TextArea notesText;
    private HorizontalLayout noteMenu;
    private VerticalLayout checkbox;

    public NoteComponent(Note note,NoteService noteService){
        this.note = note;
        this.noteService = noteService;
        stylingThisComponent();
        this.noteHeader = createNoteHeader(notesTitle);
        this.checkbox = createCheckBox();
        this.notesText = createNotesText(note);
        this.noteMenu = createNoteMenu();

        this.add(noteHeader, notesText, checkbox, noteMenu);
    }

    //Methods of NoteComponents
    protected HorizontalLayout createNoteMenu() {
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        noteMenu.add(
                toTrashButton(note),
                getUpdateChanges(),
                getArchiveButton(note)
                );
        return noteMenu;
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
    private Button getArchiveButton(Note note) {
        Button archiveButton = new Button("Archive");
        archiveButton.addClickListener(click -> {
            toArchiveNote(note);
            // this.updatePage();
        });
        return archiveButton;
    }

    protected Button toTrashButton(Note note) {
        Button toTrashButton = new Button("Move to trash");
        toTrashButton.addClickListener(click -> {
            toTrash(note);
            // this.updatePage();
        });
        return toTrashButton;
    }

    private  TextArea createNotesText(Note note) {
        TextArea notesText = new TextArea();
        notesText.setValue(note.getText());
        notesText.setLabel("Notes text");
        notesText.getStyle().setMargin("0");
        notesText.getStyle().setDisplay(Style.Display.BLOCK);
        notesText.setVisible(true);
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

    private HorizontalLayout createNoteHeader(TextField notesText) {
        HorizontalLayout noteHeader =  new HorizontalLayout();
        noteHeader.getStyle().setMargin("0px 0px 0px 0px");
        noteHeader.getStyle().setPadding("0px 0px 0px 0px");
        noteHeader.setVerticalComponentAlignment(Alignment.CENTER);
        Button pin = getPinButton();
        pin.getStyle().setFloat(Style.FloatCss.RIGHT);

        notesTitle = new TextField();
        notesTitle.setValue(this.note.getTitle());
        notesTitle.getStyle().setMargin("0");
        notesTitle.getStyle().setPadding("0px");
        noteHeader.add(notesTitle, pin);
        return noteHeader;
    }

    private void stylingThisComponent() {
        this.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.getStyle().setBoxShadow("1px 1px 3px linen");
        this.setWidth("30%");
        this.setMargin(true);
    }

    private Button getPinButton() {
        Icon pinIcon = new Icon("pin");
        String pinIconColor = this.note.isPinned() ? "black" : "gray";
        pinIcon.setColor(pinIconColor);
        Button pinButton = new Button(pinIcon);
        pinButton.getStyle().setFloat(Style.FloatCss.RIGHT);
        String tooltipText = this.note.isPinned() ? "Unpin" : "Pin";
        pinButton.setTooltipText(tooltipText);
        pinButton.addClickListener(e -> {
            this.noteService.togglePin(this.note.getId());
          //  this.updatePage();
        });
        return pinButton;
    }

    protected Button getUpdateChanges() {
        Button updateChangesButton = new Button("Save");
        updateChangesButton.addClickListener(click -> {
            updateNotesChanges();
          //  this.updatePage();
        });
        return updateChangesButton;
    }

    private void toArchiveNote(Note note) {
        this.noteService.archiveNote(note.getId());
        this.noteService.unpinNote(note.getId());
    }

    private void toTrash(Note note) {
        this.noteService.moveToTrash(note);
        this.noteService.unpinNote(note.getId());
    }

    private void updateNotesChanges(){
        note.setTitle(this.notesTitle.getValue());
        note.setText(this.notesText.getValue());
        this.noteService.saveNote(note);
        this.note = this.noteService.findById(note.getId());

        this.removeAll();
        this.add(createNoteHeader(notesTitle), createNotesText(note), createCheckBox(), createNoteMenu());
    }
}