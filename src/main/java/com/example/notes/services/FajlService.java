package com.example.notes.services;

import com.example.notes.data.Fajl;
import com.example.notes.data.Note;
import com.example.notes.repository.FajlRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    public List<Fajl> getAllFiles(){
        return this.fajlRepository.findAll();
    }

    public List<Fajl> getNoteFiles(Note note){
       return this.fajlRepository.findByNote(note);
    }
}
