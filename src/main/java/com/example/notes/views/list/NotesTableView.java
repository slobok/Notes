package com.example.notes.views.list;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "table-view",layout = MainLayout.class)
public class NotesTableView  extends VerticalLayout {
    private final NoteService noteService;
    NotesTableView(NoteService noteService){
        this.noteService = noteService;
        Grid<Note> grid = new Grid<>(Note.class, false);
        grid.addColumn(Note::getNoteId).setHeader("Notes_Id");
        grid.addColumn(Note::getTitle).setHeader("Notes_title");
        grid.addColumn(Note::getText).setHeader("Notes_text");
        grid.addColumn(Note::getNoteColor).setHeader("Notes_color");


        //Pogledaj sta radi funkcija ispod isklučuje renderovanje
        //grid.setAllRowsVisible(true);


        DataProvider<Note, Void> dataProvider =
                DataProvider.fromCallbacks(
                        // First callback fetches items based on a query
                        query -> {
                            // The index of the first item to load
                            int offset = query.getOffset();
                            // The number of items to load
                            int limit = query.getLimit();

                            List<Note> noteList = noteService.getNotesWithOffsetAndLimit(offset,limit);
                            return noteList.stream();
                        },
                        // Second callback fetches the total number of items currently in the Grid.
                        // The grid can then use it to properly adjust the scrollbars.
                        query -> noteService.countNotes()
                );

        // List<Note> allNotes = noteService.getAllNotes("");
        //  grid.setItems(allNotes);
        //  grid.setPageSize(4000);
        grid.setDataProvider(dataProvider);
        add(grid);

    }

}