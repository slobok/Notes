package com.example.notes.services;

import com.example.notes.data.Fajl;
import com.example.notes.data.FileContentDb;
import com.example.notes.data.Note;
import com.example.notes.repository.FajlRepository;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FajlService {

    private final FajlRepository fajlRepository;
    private final FileContentService fileContentService;
    public FajlService(FajlRepository fajlRepository,FileContentService fileContentService) {
        this.fajlRepository = fajlRepository;
        this.fileContentService = fileContentService;

    }

 /*   public void saveFileToDB(MultipartFile multipartFile, Note note) throws Exception{
        Fajl file = new Fajl();
        file.setNote(note);
        file.setFileName(multipartFile.getName());
        file.setFileType(multipartFile.getContentType());
        file.setData(multipartFile.getBytes());
        file.setFileSize(multipartFile.getSize());
        fajlRepository.save(file);
    }*/

/*    @Transactional
    public void saveFile(InputStream fajl,Note note, String fileName) throws IOException {
        Fajl fajl1 = new Fajl();
        fajl1.setNote(note);
        fajl1.setFileName(fileName);
        fajl1.setDataUsingInputStream(fajl);
        fajlRepository.save(fajl1);
    }*/


    public List<Fajl> getAllFiles() {
        return this.fajlRepository.findAll();
    }

    public List<Fajl> getNoteFiles(Note note) {
        return this.fajlRepository.findByNote(note);
    }

    public List<FileContentDb> getFileContentents(Note note){
        return getNoteFiles(note).stream().map(Fajl::getFileContentDb).collect(Collectors.toList());
    }

    public Stream<Fajl> getFilesByIdAndNote(List<Long> fileIds, Note note) {
        return getNoteFiles(note).stream().filter(fajl -> fileIds.contains(fajl.getFileId()));
    }


    public void deleteFile(Fajl fajl) throws Exception {
        File file = new File(fajl.getFilePath());
        if (file.delete()) {
            this.fajlRepository.delete(fajl);
        } else {
            throw new Exception("File - " + fajl.getFileName() + " not deleted");
        }
    }


    public void saveFileAndFileData(Fajl fajl,InputStream inputStream, Long fileSize){
        FileContentDb fileContentDb = this.fileContentService.saveAsInputStream(inputStream, fileSize);
        fajl.setFileContentDb(fileContentDb);
        saveFile(fajl);
    }

    public void saveFile(Fajl fajl) {
        this.fajlRepository.save(fajl);
    }

    public void saveAll(List<Fajl> fajls) {
        this.fajlRepository.saveAll(fajls);
    }

}
