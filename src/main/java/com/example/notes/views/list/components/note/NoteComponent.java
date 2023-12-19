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

    public NoteComponent(Note note, NoteService noteService){
        this.noteService = noteService;
        this.note = note;
        stylingThisComponent();
        this.noteMenu = createNoteMenu();
        updateNote();
        this.add(noteHeader, notesText, checkbox, noteMenu);
    }

    private void updateNote() {
        this.notesText = createNotesText();
        this.checkbox = createCheckBox();
        this.noteHeader = createNoteHeader();
    }

    //Methods of NoteComponents
    protected HorizontalLayout createNoteMenu() {
        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        noteMenu.add(
                toTrashButton(),
                getUpdateChanges(),
                getArchiveButton()
                );
        return noteMenu;
    }


    private  TextArea createNotesText() {
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
        noteHeader.add(notesTitle, pin);
        return noteHeader;
    }

    private void stylingThisComponent() {
        this.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.getStyle().setBoxShadow("1px 1px 3px linen");
        this.setWidth("30%");
        this.setMargin(true);
    }

    protected Button toTrashButton() {
        Button toTrashButton = new Button(new Icon("trash"));
        toTrashButton.setTooltipText("Move this note to trash");
        toTrashButton.addClickListener(click -> {
            toTrash();
            // vjerovatno update
        });
        return toTrashButton;
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

    private Button getArchiveButton() {
        Button archiveButton = new Button(new Icon("archive"));
        archiveButton.setTooltipText("Archive note");
        archiveButton.addClickListener(click -> {
            toArchiveNote();
            // this.updatePage();
        });
        return archiveButton;
    }

    protected Button getUpdateChanges() {
        Button updateChangesButton = new Button("Save");
        updateChangesButton.setTooltipText("Save note changes");
        updateChangesButton.addClickListener(click -> {
            updateNotesChanges();
          //  this.updatePage();
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
        this.add(noteHeader, notesText, checkbox, noteMenu);
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
}
