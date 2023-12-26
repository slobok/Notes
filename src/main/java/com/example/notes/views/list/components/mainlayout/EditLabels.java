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
        add(editLabels);
        updateLabelsInDrawer();
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
        HorizontalLayout addNewLabelRow = new HorizontalLayout();
        Button addLabelButton = new Button(new Icon("plus"));
        TextField newLabel = new TextField();
        addLabelButton.addClickListener(e -> {
            labelService.addLabel(newLabel.getValue());

            newLabel.setValue("");
            labelList.removeAll();
            labelList.add(addNewLabelRow, getAllLabels());
            //Zasto ovo radim?Volim da vidim kada dovucem iz baze sve da li je sve ok.
            updateLabelsInDrawer();
        });
        addNewLabelRow.add(newLabel, addLabelButton);
        labelList.add(addNewLabelRow, getAllLabels());
        dialogLabels.add(labelList);
        return dialogLabels;
    }

    private void updateLabelsInDrawer() {
        this.remove(labelsInDrawer);
        labelsInDrawer = getLabelsInDrawer();
        this.add(labelsInDrawer);
    }

    private VerticalLayout getAllLabels(){
        VerticalLayout labelsList = new VerticalLayout();

        //Label row ispod treba da sadrzi delete ikonicu za brisanje labele,
        //kao save dugme za cuvanje izmjena imena.

        this.labelService.getAllLabels().forEach(label -> {
            // Ime labele smještam u textfield
            HorizontalLayout labelRow = new HorizontalLayout();
            labelRow.getStyle().setPadding("0");
            labelRow.getStyle().setMargin("0 1%");
            TextField labelName = new TextField();
            labelName.setValue(label.getName());
            Button deleteLabelButtonInDialog  = new Button(new Icon("close-circle"));
            deleteLabelButtonInDialog.setTooltipText("Delete label");
            deleteLabelButtonInDialog.addClickListener(click -> {
                this.labelService.deleteLabel(label);
                labelRow.removeFromParent();
                updateLabelsInDrawer();
            });
            Button saveLabel = new Button(new Icon("check-circle-o"));
            saveLabel.setTooltipText("Sava name changes");
            saveLabel.addClickListener(click ->{
                label.setName(labelName.getValue());
                this.labelService.saveLabel(label);
                updateLabelsInDrawer();
            });
                    // U svakom redu potrebni imati delete ikonicu, ime labele i trece dugme?
            labelRow.add(deleteLabelButtonInDialog, labelName, saveLabel);
            labelsList.add(labelRow);
            // Potrebno staviti u jedan red x za brisanje labelee

        });
        return labelsList;
    }
}