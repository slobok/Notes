package com.example.notes.views.list.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class PaginationComp extends HorizontalLayout {
    Tabs tabs;
    int numberOfNotes;
    int numberOfItemsPerPage;
    public  PaginationComp(int numberOfNotePerPage, int numberOfNotes){
        setNumberOfNotes(numberOfNotes);
        setNumberOfItemsPerPage(numberOfNotePerPage);
        add(makeTabs());
    }


    public Tabs makeTabs() {
        Tabs tabs = new Tabs();
        for (int index = 1; index <=  (int) Math.floor(( (double) numberOfNotes / (double) numberOfItemsPerPage )); index += 1){
            Tab tabNumber = new Tab(""   + index);
            tabs.add(tabNumber);
        }
        tabs.setMaxWidth("250px");
        return  tabs;
    }

    public void setNumberOfItemsPerPage(int numberOfItemsPerPage) {
        this.numberOfItemsPerPage = numberOfItemsPerPage;
    }

    public void setNumberOfNotes(int numberOfNotes) {
        this.numberOfNotes = numberOfNotes;
    }
}
