package com.example.Notes.views.List;

import com.example.Notes.Data.Note;
import com.example.Notes.Services.NoteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "",layout = MainLayout.class)
public class NotesList extends VerticalLayout {
    protected NoteService noteService;
    private TextField search = new TextField();

    public TextField getSearch() {
        return search;
    }

    NotesList(NoteService noteService){
        this.noteService = noteService;
        addToConstructor();
    }

    protected void addToConstructor(){
        this.add(
                getSearchField(),
                createNoteField(),
                getAllNotes()
        );
    }

    protected Component getSearchField() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidthFull();
        this.search.setPlaceholder("Search");
        this.search.setPrefixComponent(new Icon("lumo","search"));
        search.addValueChangeListener(e -> {
            this.updatePage();
        });
        hl.add(search);
        return  hl;
    }

    private Component createNoteField(){
        VerticalLayout newNote = new VerticalLayout();
        newNote.setAlignItems(Alignment.CENTER);
        newNote.getStyle().setBoxShadow("2px 2px 4px linen");

        TextField notesTitle = new TextField();
        notesTitle.setPlaceholder("Title");
        notesTitle.setVisible(false);
        notesTitle.setWidth("30%");

        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Type here");
        textArea.setWidth("30%");
        textArea.addFocusListener(e -> {
            notesTitle.setVisible(true);
        });

        HorizontalLayout noteMenu = new HorizontalLayout();
        noteMenu.setAlignItems(Alignment.START);
        noteMenu.getStyle().setBorder("1px solid black");
        Icon imageIcon = new Icon("lumo","photo");
        noteMenu.add(imageIcon);

        Button createNote = new Button("Create note");
        createNote.addClickListener(click -> {
            this.noteService.saveNote(new Note(notesTitle.getValue(), textArea.getValue(),1L));
              notesTitle.setValue("");
              textArea.setValue("");
              this.updatePage();
        });
        newNote.add(notesTitle, textArea, createNote);
        return newNote;
    }

    // Metoda koja dovlaci iz baze sve notese
    protected VerticalLayout getAllNotes(){
        VerticalLayout allNotes = new VerticalLayout();

        VerticalLayout pinnedNotesList = new VerticalLayout();
        pinnedNotesList.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        pinnedNotesList.add(new H6("Pinned"));
        pinnedNotesList.setVisible(false);

        VerticalLayout notesList = new VerticalLayout();
        notesList.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        notesList.add(new H6("Other notes:"));

        this.noteService.getAllNotes(this.search.getValue()).forEach(n -> {
            //Note
            VerticalLayout note = new VerticalLayout();
            note.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
            note.getStyle().setBoxShadow("2px 2px 4px 4px linen");

            note.setWidth("30%");
            note.setMargin(true);

            //Dodavanje pin button-a i funkcionalnosti
            String tooltipText = n.isPinned() ? "Unpin" : "Pin";
            String pinIconColor = n.isPinned() ? "black" : "gray";
            Icon pinIcon = new Icon("pin");
            pinIcon.setColor(pinIconColor);
            Button pin = new Button(pinIcon);
            pin.getStyle().setFloat(Style.FloatCss.RIGHT);
            pin.setTooltipText(tooltipText);
            pin.addClickListener(e -> {
                this.noteService.changeIsPinned(n.getId());
                this.updatePage();
            });

            TextField notesTitle = new TextField();
            notesTitle.setValue(n.getTitle());
            notesTitle.setLabel("Title");
            notesTitle.getStyle().setMargin("0");
            notesTitle.getStyle().setDisplay(Style.Display.BLOCK);

                //Nema potrebe za ovim ako nije checkboxform
                VerticalLayout checkbox = new VerticalLayout();
                checkbox.getStyle().setPadding("0px");
                checkbox.getStyle().setMargin("0px");


                // TextField addToCheckBox = new TextField();
              //  addToCheckBox.getStyle().setMargin("0");
              //  addToCheckBox.getStyle().setPadding("0px");
             //   addToCheckBox.setHelperText("add to notes");
                List<String> wordsList = new ArrayList<>(List.of(n.getText().split("\\n")));
                CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
                checkboxGroup.setItems(wordsList);
                checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

                checkbox.add(checkboxGroup);

                //Za ovim nema potrebe ako je chechboxform notes
                TextArea notesText = new TextArea();
                notesText.setValue(n.getText());
                notesText.setLabel("Notes text");
                notesText.getStyle().setMargin("0");
                notesText.getStyle().setDisplay(Style.Display.BLOCK);
                notesText.setVisible(true);

            Button deleteButton = new Button("Move to trash");
            deleteButton.addClickListener(click -> {
                this.noteService.moveToTrash(n);
                this.noteService.unpinNote(n.getId());
                this.updatePage();
            });
            Button archiveButton = new Button("Archive");
            archiveButton.addClickListener(click -> {
                this.noteService.updateStateIsArchived(n.getId());
                this.noteService.unpinNote(n.getId());
                this.updatePage();
            });

            Button updateChanges = new Button("Save");
            updateChanges.addClickListener(click -> {
                Note noteToUpdate = new Note();
                noteToUpdate.setId(n.getId());
                noteToUpdate.setTitle(notesTitle.getValue());
                noteToUpdate.setText(notesText.getValue());
                noteToUpdate.setCreatedByUser(n.getCreatedByUser());
                noteToUpdate.setIsTrashed(n.getIsTrashed());
                noteToUpdate.setIsArchived(n.getIsArchived());
                noteToUpdate.setPinned(n.isPinned());
                this.noteService.saveNote(noteToUpdate);
                this.updatePage();
            });

            Button showHideCheckBoxes = new Button("Show checkboxes");
            showHideCheckBoxes.setEnabled(false);
            showHideCheckBoxes.addClickListener(click -> {
                //this.noteService.setIsInChechBoxSyle(n);
            });

            MenuBar menuBar = new MenuBar();
            MenuItem moreItem = menuBar.addItem(new Icon("ellipsis-v"));
            SubMenu moreItemSubMenu = moreItem.getSubMenu();
            moreItemSubMenu.addItem("To trash");

            //More icon kao i u google notes
            Button more = new Button(new Icon("ellipsis-v"));

            HorizontalLayout noteMenu = new HorizontalLayout();
            noteMenu.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
            noteMenu.add(deleteButton, updateChanges, archiveButton, showHideCheckBoxes,more,menuBar);
            note.add(pin,notesTitle, notesText, checkbox, noteMenu);

            if(n.isPinned()){
                pinnedNotesList.add(note);
                pinnedNotesList.setVisible(true);
            }
            else {
                notesList.add(note);
            }
            allNotes.add(pinnedNotesList, notesList);
                }
        );
     return allNotes;
    }

    protected void updatePage(){
        this.removeAll();
        this.add(
                this.getSearchField(),
                this.createNoteField(),
                this.getAllNotes()
        );
    }
}