package com.example.notes.views.list.components.note.NoteEvents;

import com.example.notes.data.Fajl;
import com.example.notes.data.Note;
import com.example.notes.services.FajlService;
import com.example.notes.services.NoteService;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class NoteComponentsEvents {
    private final FajlService fajlService;
    NoteComponentsEvents(FajlService fajlService, NoteService noteService){
        this.fajlService = fajlService;
    }

    public void saveFileAndFileContentEvent(MultiFileBuffer multiFileBuffer, SucceededEvent event, Note note){
        InputStream fileData = multiFileBuffer.getInputStream(event.getFileName());
        Fajl fajl = new Fajl(event.getFileName(), event.getMIMEType(), event.getContentLength(), note);
        this.fajlService.saveFileAndFileData(fajl, fileData, event.getContentLength());
    }

        public void saveFileOnFileSystem(MultiFileBuffer multiFileBuffer, SucceededEvent event, Note note){
            InputStream fileData = multiFileBuffer.getInputStream(event.getFileName());
              /*  try {
                this.fajlService.saveFile(fileData, note, event.getFileName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
        }

        public void saveFileAsMultipart(MultiFileBuffer multiFileBuffer, SucceededEvent event, Note note){
           /* InputStream fileData = multiFileBuffer.getInputStream(event.getFileName());
               try {
            MultipartFile multipartFile = new MockMultipartFile(event.getFileName(),
                        event.getFileName(),event.getMIMEType(), IOUtils.toByteArray(fileData));
            fajlService.saveFileToDB(multipartFile, note);
            System.out.println("File saved to db");
                }
            catch (Exception e) {
                 throw new RuntimeException(e);
                }*/
        }

}
