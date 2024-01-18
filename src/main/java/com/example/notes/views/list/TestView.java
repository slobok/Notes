package com.example.notes.views.list;


import com.example.notes.services.NoteService;
import com.vaadin.flow.router.Route;

@Route(value = "test")
public class TestView {
    private final NoteService noteService;

    public TestView(NoteService noteService){
        this.noteService = noteService;
    }




}
