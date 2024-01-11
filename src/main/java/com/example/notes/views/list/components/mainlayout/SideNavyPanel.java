package com.example.notes.views.list.components.mainlayout;

import com.example.notes.data.Label;
import com.example.notes.services.NoteService;
import com.example.notes.views.list.ArchivedNotes;
import com.example.notes.views.list.NotesList;
import com.example.notes.views.list.TrashedNotes;
import com.example.notes.views.list.events.CountingNotesEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.DataProviderListener;
import com.vaadin.flow.data.provider.DataProviderWrapper;
import com.vaadin.flow.data.provider.ListDataProvider;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.awt.*;
import java.lang.module.Configuration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SideNavyPanel extends Div {
    Integer numberOfNotesInTrash;
    Integer numberOfNotesInArchive;
    Integer numberOfNotes;
    NotesCounter countNotes;
    NotesCounter countArchivedNotes;
    NotesCounter countTrashedNotes;
    private final NoteService noteService;
    public SideNavyPanel(NoteService noteService) {
        this.noteService = noteService;
        setNumberOfNotes();
        // todo Nema potrebe da imam tri podatka Integer, mislim može bez njih
        SideNav nav = new SideNav();
        SideNavItem notesLink = new SideNavItem("Notes", NotesList.class, VaadinIcon.NOTEBOOK.create());
        SideNavItem archivedNotesLink = new SideNavItem("Archive", ArchivedNotes.class, VaadinIcon.ARCHIVE.create());
        SideNavItem trashedNotesLink = new SideNavItem("Trash", TrashedNotes.class, VaadinIcon.TRASH.create());

        initializeNotesCounters();

        notesLink.setSuffixComponent(countNotes);
        archivedNotesLink.setSuffixComponent(countArchivedNotes);
        trashedNotesLink.setSuffixComponent(countTrashedNotes);

        nav.addItem(notesLink, archivedNotesLink, trashedNotesLink);
        nav.setCollapsible(true);
        nav.setLabel("Groups");

        Div navWrapper = new Div(nav);
        nav.setWidthFull();
        add(navWrapper);
        updateNumbersOfNotes();

    }

    private void updateNumbersOfNotes() {
        ComponentUtil.addListener(UI.getCurrent(), CountingNotesEvent.class, event -> {
            setNumberOfNotes();
            updateNumbersOfNotesCounters(countNotes, countArchivedNotes, countTrashedNotes);
        });
    }

    private void initializeNotesCounters() {
        countNotes = new NotesCounter(numberOfNotes.toString());
        countArchivedNotes = new NotesCounter(numberOfNotesInArchive.toString());
        countTrashedNotes = new NotesCounter(numberOfNotesInTrash.toString());
    }

    private void updateNumbersOfNotesCounters(NotesCounter countNotes, NotesCounter countArchivedNotes, NotesCounter countTrashedNotes) {
        countNotes.setText(numberOfNotes.toString());
        countArchivedNotes.setText(numberOfNotesInArchive.toString());
        countTrashedNotes.setText(numberOfNotesInTrash.toString());
    }

    private void setNumberOfNotes(){
        numberOfNotes = noteService.countNotes();
        numberOfNotesInArchive = noteService.countNotesInArchive();
        numberOfNotesInTrash = noteService.countNotesInTrash();
    }
}
