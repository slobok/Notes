package com.example.notes.views.list;

import com.example.notes.data.Note;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.components.NewNoteForm;
import com.example.notes.views.list.components.note.NoteComponent;
import com.example.notes.views.list.components.note.NoteEvents.NoteClickListeners;
import com.example.notes.views.list.components.note.NoteEvents.NotesComponents;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;


import java.util.ArrayList;
import java.util.List;

@Route(value = "virtual-test",layout = MainLayout.class)
public class VirtualLIst extends Div {

    private final NoteService noteService;
    private final NotesComponents noteComponents;
    private final NoteClickListeners noteClickListeners;
    public VirtualLIst(NoteService noteService, NotesComponents noteComponents, NoteClickListeners noteClickListeners){
        this.noteService = noteService;
        this.noteComponents = noteComponents;
        this.noteClickListeners = noteClickListeners;
        List<Note> noteList = noteService.getAllNotes("");


        List<List<Note>> listOfNotesList = new ArrayList<>();
        List<Note>  noteList4 = new ArrayList<>();

        for (int index = 1; index <= noteList.size(); index++) {
            noteList4.add(noteList.get(index - 1));
            if(index != 0 && index % 3 == 0){
                listOfNotesList.add(noteList4);
                noteList4 = new ArrayList<>();
            }
        }
        listOfNotesList.add(noteList4);
        ListDataProvider<List<Note>> noteListDataProvider = new ListDataProvider<>(listOfNotesList);


        ComponentRenderer <Component, List<Note> > noteRenderer = new ComponentRenderer<>(notes  -> {
            Div div = new Div();
            div.setWidth("100%");
            notes.forEach(note -> {
                div.add(new NoteComponent(note, noteComponents, noteClickListeners));
            });
            return div;
        }
        );

        VirtualList<List< Note> > virtualList = new VirtualList<>();
       // virtualList.setDataProvider(noteListDataProvider);

        virtualList.setItems(listOfNotesList);
        virtualList.getStyle().setHeight("90vh");
        virtualList.setWidthFull();

        virtualList.setRenderer(noteRenderer);
        Button refreshButton = new Button("test");
/*        refreshButton.addClickListener(event -> {
                noteListDataProvider.getItems().clear();
                noteListDataProvider.getItems().addAll(noteService.getAllNotes(""));
                noteListDataProvider.refreshAll();

        });*/

        VerticalLayout vl = new VerticalLayout();
        vl.add(virtualList);
        add(refreshButton);
        add(new NewNoteForm(noteService));

        add(vl);
        this.getStyle().setWidth("100%");
    }
}
