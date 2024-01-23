package com.example.notes.views.list;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

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

        grid.setAllRowsVisible(true);

        List<Note> allNotes = noteService.getAllNotes("");
        grid.setItems(allNotes);
        grid.setPageSize(4000);

        add(grid);

    }

}
