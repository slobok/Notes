package com.example.notes.views.list;

import com.example.notes.data.Label;
import com.example.notes.data.Note;
import com.example.notes.services.LabelService;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.NewNoteForm;
import com.example.notes.views.list.components.note.NoteComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;


import java.util.List;

@Route(value = "virtual-test",layout = MainLayout.class)
public class VirtualLIst extends Div {
private final NoteService noteService;
private final LabelService labelService;
    private List<Note> noteList;

    public VirtualLIst(NoteService noteService, LabelService labelService){
        this.noteService = noteService;
        this.labelService = labelService;

        noteList = noteService.getAllNotes("");



        ComponentRenderer <Component, Note> noteRenderer = new ComponentRenderer<>(note -> {
            return new NoteComponent(note, noteService, labelService);
        });

        ListDataProvider<Note> noteListDataProvider = new ListDataProvider<>(noteService.getAllNotes(""));

        VirtualList<Note> list = new VirtualList<>();
        list.setDataProvider(noteListDataProvider);
        list.getStyle().setWidth("100%");
        
        list.getStyle().setWidth("100%");
        list.getStyle().setHeight("90vh");

     //   list.setItems(noteList);
        list.setRenderer(noteRenderer);
        Button refreshButton = new Button("test");
        refreshButton.addClickListener(event -> {
                noteListDataProvider.getItems().clear();
                noteListDataProvider.getItems().addAll(noteService.getAllNotes(""));
                noteListDataProvider.refreshAll();

        });
        add(refreshButton);
        add(new NewNoteForm(noteService));

        add(list);

    }
}
