package com.example.notes.views.list;

import com.example.notes.data.Note;
import com.example.notes.services.FajlService;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "archive",layout = MainLayout.class)
public class ArchivedNotes extends TrashedNotes {

    ArchivedNotes(NoteService noteService, LabelService labelService, FajlService fajlService) {
        super(noteService, labelService, fajlService);
    }

    @Override
    protected void addComponentsToPage(){
             add(
                     getAllNotes()
             );
    }

    @Override
    protected VerticalLayout getAllNotes()  {
        VerticalLayout notesList = new VerticalLayout();
        notesList.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        VirtualList<List<Note>> virtualList = new VirtualList<List<Note>>();
        virtualList.setRenderer(makeNotesRenderer());

        return notesList;
    }

    @Override
    protected void setNoteType(Note note, Div div) {
        super.setNoteType(note, div);
    }

    @Override
    protected void updatePage(){
        listDataProvider = new ListDataProvider<>(makeListsOfList(this.noteService.getAllArchivedNotes(getSearch().getValue())));
        listDataProvider.getItems().clear();
        listofNotesLists = makeListsOfList(noteService.getAllTrashedNotes(getSearch().getValue()));
        listDataProvider.getItems().addAll(listofNotesLists);
        listDataProvider.refreshAll();
    }
}