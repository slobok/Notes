package com.example.notes.views.list.components.note.NoteEvents;

import com.example.notes.data.Fajl;
import com.example.notes.data.FileContentDb;
import com.example.notes.data.Note;
import com.example.notes.services.FajlService;
import com.example.notes.services.FileContentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.server.StreamResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

@Component
public class NotesComponents {

    private final NoteComponentsEvents noteComponentsEvents;
    private final ZippingFiles zippingFiles;
    private final FajlService fajlService;
    private final FileContentService fileContentService;
    public NotesComponents(NoteComponentsEvents noteComponentsEvents, ZippingFiles zippingFiles, FajlService fajlService, FileContentService fileContentService){
        this.noteComponentsEvents = noteComponentsEvents;
        this.zippingFiles = zippingFiles;
        this.fajlService = fajlService;
        this.fileContentService = fileContentService;
    }

    public HorizontalLayout fileUploadWithMultiFileBuffer(Note note){
        MultiFileBuffer multiFileBuffer  = new MultiFileBuffer();
        Upload upload = new Upload(multiFileBuffer);
        upload.setMaxFileSize(2000000000);
        upload.addSucceededListener(event -> noteComponentsEvents.saveFileAndFileContentEvent(multiFileBuffer, event, note));
        return new HorizontalLayout(upload);
    }

    public HorizontalLayout downloadLinkForFiles(Note note){
        Button button = new Button(VaadinIcon.FILE.create());
        button.addClickListener(event -> {
            makeDialog(note).open();
        });
        return  new HorizontalLayout(button);
    }

    protected Dialog makeDialog(Note note) {
        Dialog dialog = new Dialog();
        Button closeDialog = new Button(VaadinIcon.CLOSE.create());
        closeDialog.getStyle().setFloat(Style.FloatCss.RIGHT);
        closeDialog.addClickListener(event -> {
            dialog.close();
        });
        dialog.add(closeDialog);
        dialog.setCloseOnOutsideClick(true);
        VerticalLayout layoutLinks = new VerticalLayout();
        List<Fajl> noteFiles = fajlService.getNoteFiles(note);
        noteFiles.forEach(fajl -> {
            FileContentDb fileContentDb = this.fileContentService.getFileContentByID(fajl.getFileId());
            Anchor anchor = new Anchor(new StreamResource(fajl.getFileName(), () -> getInputStream(fileContentDb) )
                    , fajl.getFileName() + " "  + fajl.getFileSize());
            anchor.getElement().getThemeList().add("primary");
            anchor.setTarget("_blank");
            anchor.getElement().setAttribute("download", true);
            layoutLinks.add(anchor);
        });
        dialog.add(layoutLinks);
        Button downloadSelected = new Button(VaadinIcon.DOWNLOAD.create());

        // Zamijeni redosled ovoga mozda da se ne ide do baze ako nema notesa

        downloadSelected.setVisible(!noteFiles.isEmpty());
        downloadSelected.addClickListener(event -> {
            try {
               zippingFiles.makeZipFromFiles(note);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        dialog.add(new HorizontalLayout(downloadSelected));
        return dialog;
    }

    private static InputStream getInputStream(FileContentDb fileContentDb) {
        InputStream inputStream;
        try {
            inputStream = fileContentDb.getData().getBinaryStream();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return inputStream;
    }

    /*private com.vaadin.flow.component.Component makeLabelBox(){
        MultiSelectComboBox<Label> labelMultiSelectComboBox = new MultiSelectComboBox<Label>("Labels");
        // Todo napravi ovu kompoentu kako bi mogao da koristis allLabels unutare eventa

        allLabels = new ArrayList<>(labelService.getAllLabels());
        labelMultiSelectComboBox.setItems(allLabels);
        labelMultiSelectComboBox.setItemLabelGenerator(Label::getName);

        ComponentUtil.addListener(UI.getCurrent(), LabelsUpdateEvent.class,(ComponentEventListener<LabelsUpdateEvent>) event -> {
            allLabels = new ArrayList<>(labelService.getAllLabels());
            labelMultiSelectComboBox.setItems(allLabels);
            labelMultiSelectComboBox.setItemLabelGenerator(Label::getName);
            List<Long> selectedLabels = noteService.getNoteLabels(note.getNoteId()).stream().map(
                    Label::getLabelId
            ).toList();
            System.out.println("Selected labels +  " + selectedLabels);
            allLabels.forEach(label -> {
                if(selectedLabels.contains(label.getLabelId())){
                    System.out.println("usao u petlju selected " + label);
                    labelMultiSelectComboBox.select(label);
                }
            });
        });

        List<Long> selectedLabels =  noteService.getNoteLabels(note.getNoteId()).stream().map(
                Label::getLabelId
        ).toList();
        System.out.println(selectedLabels);
        allLabels.forEach(label -> {
            if(selectedLabels.contains(label.getLabelId())){
                labelMultiSelectComboBox.select(label);
            }
            labelMultiSelectComboBox.addSelectionListener(event -> {
                if(!event.getAddedSelection().isEmpty()){
                    ArrayList<Label> addedLabel = new ArrayList<>(event.getAddedSelection());
                    this.noteService.addLabelsToNote(note, addedLabel);
                }
                else if(!event.getRemovedSelection().isEmpty()) {
                    ArrayList<Label> labelList = new ArrayList<>(event.getRemovedSelection());
                    this.noteService.removeLabelSFromNote(note, labelList);
                }
            });
        });

        return labelMultiSelectComboBox;
    }*/

      /*  protected Dialog makeNewDialog(){
        Dialog dialog = new Dialog();
        Grid<Fajl> grid = new Grid<>(Fajl.class,false);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addColumn(Fajl::getFileName).setHeader("File name");
        grid.addColumn(Fajl::getFileType).setHeader("File type");

        ListDataProvider<Fajl> listDataProvider = new ListDataProvider<>(fajlService.getNoteFiles(note));
        grid.setDataProvider(listDataProvider);

        dialog.setWidth("70%");
        dialog.add(grid);
        Button downloadButton = new Button(VaadinIcon.DOWNLOAD.create());
        downloadButton.setEnabled(false);
        grid.addSelectionListener(event -> {
            downloadButton.setEnabled(!grid.getSelectedItems().isEmpty());
        });

        downloadButton.addClickListener(event -> {
          List <Long> listIds =   grid.getSelectedItems().stream().map(Fajl::getFileId).toList();
            try {
                makeZipFromFiles(fajlService.getFilesByIdAndNote(listIds, note).collect(Collectors.toList()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Button deleteSelectedFiles = new Button(VaadinIcon.DEL.create());
        deleteSelectedFiles.addClickListener(event -> {
            grid.getSelectedItems().stream().forEach( fajl -> {
                System.out.println(fajl.getFileName());
                   try{
                       this.fajlService.deleteFile(fajl);
                   }
                   catch (Exception e){
                       System.out.println(e.getMessage());
                   }
            });

            listDataProvider.getItems().clear();
            listDataProvider.getItems().addAll(this.fajlService.getNoteFiles(note));
            listDataProvider.refreshAll();
        });

        dialog.add(downloadButton, deleteSelectedFiles);
        return dialog;
    }*/

   /* protected void makeZipFromFilesOnFileSystem(List<Fajl> selectedFiles){
        this.fajlService.getNoteFiles(note).forEach(fajl -> {

        });

    }*/
}
