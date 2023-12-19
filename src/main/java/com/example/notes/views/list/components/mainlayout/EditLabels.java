package com.example.notes.views.list.components.mainlayout;

import com.example.notes.services.LabelService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class EditLabels extends VerticalLayout {
    private final LabelService labelService;


    public EditLabels(LabelService labelService){
        this.labelService = labelService;

        //Button editLabel. Na klik treba da prikaze dialog componenta.Ona treba da sadrzi polje za unos nove labele
        // i listu labele koje sa azuriraju
        Button editLabels = new Button("Edit Labels");
        Dialog dialogLabels = getDialogLabels();
        editLabels.addClickListener(e -> dialogLabels.open());
        add(editLabels);
    }


    private Dialog getDialogLabels() {
        Dialog dialogLabels = new Dialog();
        dialogLabels.setHeaderTitle("Edit labels:");
        //
        VerticalLayout labelList = new VerticalLayout();
        HorizontalLayout addNewLabel = new HorizontalLayout();
        Button addLabelButton = new Button(new Icon("plus"));
        TextField newLabel = new TextField();
        addLabelButton.addClickListener(e -> {
            labelService.addLabel(newLabel.getValue());

            newLabel.setValue("");
            labelList.removeAll();
            labelList.add(addNewLabel, getAllLabels());
          //  this.updateDrawerLabelList();
        });
        addNewLabel.add(newLabel, addLabelButton);
        labelList.add(addNewLabel, getAllLabels());
        dialogLabels.add(labelList);
        return dialogLabels;
    }

    private VerticalLayout getAllLabels(){
        VerticalLayout labelsList = new VerticalLayout();
        this.labelService.getAllLabels().forEach(label -> {
            TextField labelName = new TextField();
            labelName.setValue(label.getName());
            labelsList.add(labelName);
        });
        return labelsList;
    }
}
