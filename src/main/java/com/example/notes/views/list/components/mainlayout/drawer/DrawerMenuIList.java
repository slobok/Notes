package com.example.notes.views.list.components.mainlayout.drawer;

import com.example.notes.views.list.ArchivedNotes;
import com.example.notes.views.list.NotesList;
import com.example.notes.views.list.TrashedNotes;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class DrawerMenuIList extends VerticalLayout {
   public DrawerMenuIList(){
        add(
                new RouterLink("Notes", NotesList.class),
                new RouterLink("Archive", ArchivedNotes.class),
                new RouterLink("Trash", TrashedNotes.class)
        );
    }
}