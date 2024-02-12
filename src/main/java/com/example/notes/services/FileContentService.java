package com.example.notes.services;

import com.example.notes.data.Fajl;
import com.example.notes.data.FileContentDb;
import com.example.notes.repository.FileContentDbRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;


@Service
public class FileContentService {

    private final SessionFactory sessionFactory;
    private final FileContentDbRepository fileContentDbRepository;

    public FileContentService(SessionFactory sessionFactory, FileContentDbRepository fileContentDbRepository) {
        this.sessionFactory = sessionFactory;
        this.fileContentDbRepository = fileContentDbRepository;
    }

    public void save(FileContentDb fileContentDb){
        this.fileContentDbRepository.save(fileContentDb);
    }

    public FileContentDb saveAsInputStream(InputStream inputStream, Long fileSize){
        Session session = sessionFactory.openSession();
        FileContentDb fileContentDb  = new FileContentDb();
        fileContentDb.setData(session.getLobHelper().createBlob(inputStream, fileSize));
        save(fileContentDb);
        return fileContentDb;
    }

    public FileContentDb getFileContent(Fajl fajl){
        return this.fileContentDbRepository.findByFajl(fajl)
                .orElseThrow(() -> new RuntimeException("Not found file content for" + fajl.getFileName()));
    }

    public FileContentDb getFileContentByID(Long fileId){
        return this.fileContentDbRepository.findById(fileId)
                .orElseThrow(() ->  new RuntimeException("Not found contend db"));
    }

}
