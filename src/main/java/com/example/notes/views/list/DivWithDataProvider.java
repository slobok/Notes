package com.example.notes.views.list;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.stream.Collectors;

public class DivWithDataProvider <T> extends Composite<Div> {
    private final DataProvider<T, ?> dataProvider;

    public DivWithDataProvider(DataProvider<T, ?> dataProvider) {
        this.dataProvider = dataProvider;
        ListDataProvider<T> listDataProvider = new ListDataProvider<>(List.copyOf(dataProvider.fetch(new Query<>()).collect(Collectors.toList())));
        Div content = new Div();
        content.setText("Data:" + listDataProvider.getItems());
        getContent().add(content);

    }
}
