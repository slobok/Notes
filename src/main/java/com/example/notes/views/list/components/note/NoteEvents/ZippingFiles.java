package com.example.notes.views.list.components.note.NoteEvents;

import com.example.notes.data.Fajl;
import com.example.notes.data.Note;
import com.example.notes.services.FajlService;
import com.example.notes.services.FileContentService;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ZippingFiles {

    private final FajlService fajlService;
    private final FileContentService fileContentService;
    ZippingFiles(FajlService fajlService, FileContentService fileContentService){
        this.fajlService = fajlService;
        this.fileContentService = fileContentService;
    }

    protected void makeZipFromFiles(Note note) throws IOException {
        FileOutputStream fos = new FileOutputStream("Note " + note.getNoteId()  + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        List<Fajl> allFiles = fajlService.getNoteFiles(note);
        allFiles.forEach(fajl -> {
            InputStream inputStream = null;
            try {
                inputStream = this.fileContentService.getFileContentByID(fajl.getFileId()).getData().getBinaryStream();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ZipEntry zipEntry = new ZipEntry(fajl.getFileName());
            try {
                zipOut.putNextEntry(zipEntry);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] bytes = new byte[1024];
            int length;
            while(true) {
                try {
                    if (!((length = inputStream.read(bytes)) >= 0)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    zipOut.write(bytes, 0, length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                inputStream.close();
                System.out.println("File zipped");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        zipOut.close();
        fos.close();
        System.out.println("Zipping finished");
    }
}
