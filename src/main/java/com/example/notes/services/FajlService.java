package com.example.notes.services;

import com.example.notes.data.Fajl;
import com.example.notes.data.Note;
import com.example.notes.repository.FajlRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Service
public class FajlService {
    private final FajlRepository fajlRepository;
    public FajlService(FajlRepository fajlRepository){
        this.fajlRepository = fajlRepository;
    }

    public void saveFileToDB(MultipartFile multipartFile, Note note) throws Exception{
        Fajl file = new Fajl();
        file.setNote(note);
        file.setFileName(multipartFile.getName());
        file.setFileType(multipartFile.getContentType());
        file.setData(multipartFile.getBytes());
        fajlRepository.save(file);
    }
    @Transactional
    public void saveFile(InputStream fajl,Note note, String fileName) throws IOException {
        Fajl fajl1 = new Fajl();
        fajl1.setNote(note);
        fajl1.setFileName(fileName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fajl.transferTo(outputStream);
        outputStream.close();
        fajl1.setData(outputStream.toByteArray());
        fajlRepository.save(fajl1);
    }


    public List<Fajl> getAllFiles(){
        return this.fajlRepository.findAll();
    }

    public List<Fajl> getNoteFiles(Note note){
       return this.fajlRepository.findByNote(note);
    }
}
