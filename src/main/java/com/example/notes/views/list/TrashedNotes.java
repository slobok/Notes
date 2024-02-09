package com.example.notes.views.list;
import com.example.notes.data.Note;
import com.example.notes.services.FajlService;
import com.example.notes.services.FileContentService;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.note.NoteInTrash;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import org.hibernate.SessionFactory;

import java.util.List;

@Route(value = "trash",layout = MainLayout.class)
public class TrashedNotes extends NotesList  {
    List <List<Note>> listofNotesLists;
    ListDataProvider <List<Note>> listDataProvider;


    TrashedNotes(NoteService noteService, LabelService labelService, FajlService fajlService, SessionFactory sessionFactory, FileContentService fileContentService){
        super(noteService, labelService, fajlService, sessionFactory, fileContentService);
        // todo message potreban na svakoj strani, postavi na provoj
    }
    @Override
    protected void addComponentsToPage(){
        add(
                getAllNotes(),
                emptyTrashButton()
        );
    }

    private Button emptyTrashButton() {
        Button emptyTrashButton = new Button("Empty trash");
        emptyTrashButton.setVisible(false);
        emptyTrashButton.addClickListener(click -> {
            this.noteService.deleteAllInTrash();
            ComponentUtil.fireEvent(UI.getCurrent(), new CountingNotesEvent(this,false));
        });
        return emptyTrashButton;
    }

    @Override
    protected VerticalLayout getAllNotes()  {
        VerticalLayout notesList = new VerticalLayout();
        notesList.setPadding(false);
        notesList.setMargin(false);
        notesList.getStyle().setDisplay(Style.Display.INLINE_BLOCK);

        VirtualList <List<Note>> virtualList = new VirtualList<>();
        virtualList.setRenderer(super.makeNotesRenderer());
        listDataProvider = new ListDataProvider<>(makeListsOfList(getPageNotes()));
        listofNotesLists = makeListsOfList(getPageNotes());
     //   listDataProvider = new ListDataProvider<>(listofNotesLists);
        listDataProvider.getItems().clear();
        listDataProvider.getItems().addAll(listofNotesLists);
        virtualList.setDataProvider(listDataProvider);
        virtualList.setWidthFull();
        notesList.add(virtualList);
        return notesList;
    }

    protected List<Note> getPageNotes(){
        return  this.noteService.getAllTrashedNotes(getSearch().getValue());
    }

    @Override
    protected void updatePage(){
        listofNotesLists = makeListsOfList(getPageNotes());
        listDataProvider.getItems().clear();
        listDataProvider.getItems().addAll(listofNotesLists);
        listDataProvider.refreshAll();
    }

    @Override
    protected void setNoteType(Note note, Div div) {
        div.add(new NoteInTrash(note, noteService, labelService, fajlService,sessionFactory, fileContentService ));
    }
}