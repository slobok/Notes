package com.example.notes.views.list.components.mainlayout;

import com.example.notes.views.list.ArchivedNotes;
import com.example.notes.views.list.NotesList;
import com.example.notes.views.list.TrashedNotes;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class SideNavPanel extends SideNav {

    public SideNavPanel(){
        this.addItem(new SideNavItem("Notes", NotesList.class, VaadinIcon.TRASH.create()));
        this.addItem(new SideNavItem("Archive", ArchivedNotes.class, VaadinIcon.TRASH.create()));
        this.addItem(new SideNavItem("Trash", TrashedNotes.class, VaadinIcon.INBOX.create()));

    }
}
