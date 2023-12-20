package com.example.notes.views.list.components.mainlayout.drawer;

import com.example.notes.views.list.ArchivedNotes;
import com.example.notes.views.list.NotesList;
import com.example.notes.views.list.TrashedNotes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import java.util.List;

public class DrawerMenuIList extends VerticalLayout {
   public DrawerMenuIList(){
        add(
                new RouterLink("Notes", NotesList.class),
                new RouterLink("Trash", TrashedNotes.class),
                new RouterLink("Archive", ArchivedNotes.class)
        );
    }
}
