package com.example.notes.views.list.components.mainlayout;

import com.example.notes.services.LabelService;
import com.example.notes.views.list.LabeledNotes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;

public class EditLabels extends VerticalLayout {
    private final LabelService labelService;
    private VerticalLayout labelsInDrawer;

    public EditLabels(LabelService labelService){
        this.labelService = labelService;

        this.labelsInDrawer = getLabelsInDrawer();
        //Button editLabel. Na klik treba da prikaze dialog componenta.Ona treba da sadrzi polje za unos nove labele
        // i listu labele koje sa azuriraju
        Button editLabels = new Button("Edit Labels");
        Dialog dialogLabels = getDialogLabels();
        editLabels.addClickListener(e -> dialogLabels.open());
        updateLabelsInDrawer();
        add(
                editLabels,
                labelsInDrawer
                );
    }

    private VerticalLayout getLabelsInDrawer() {
        VerticalLayout listLabels = new VerticalLayout();
        this.labelService.getAllLabels().forEach(label -> {
            RouterLink link  = new RouterLink(label.getName(), LabeledNotes.class,
                    new RouteParameters("label",label.getName()));
            listLabels.add(link);
        });
        return listLabels;
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
            //Zasto ovo radim?Volim da vidim kada dovucem iz baze sve da li je sve ok.
            updateLabelsInDrawer();
            this.add(labelsInDrawer);
        });
        addNewLabel.add(newLabel, addLabelButton);
        labelList.add(addNewLabel, getAllLabels());
        dialogLabels.add(labelList);
        return dialogLabels;
    }

    private void updateLabelsInDrawer() {
        this.remove(labelsInDrawer);
        labelsInDrawer = getLabelsInDrawer();
    }

    private VerticalLayout getAllLabels(){
        VerticalLayout labelsList = new VerticalLayout();

        //Label row ispod treba da sadrzi delete ikonicu za brisanje labele,
        //kao save dugme za cuvanje immjene imena.
        HorizontalLayout labelRow = new HorizontalLayout();

        this.labelService.getAllLabels().forEach(label -> {
            TextField labelName = new TextField();
            labelName.setValue(label.getName());
            labelsList.add(labelName);
        });
        return labelsList;
    }
}