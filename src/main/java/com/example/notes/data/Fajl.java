package com.example.notes.data;

import jakarta.persistence.*;

import java.io.*;


@Entity
@Table(name="Fajl")
public class Fajl  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "file_id")
    private Long fileId;

    private String fileName;
    private String fileType;
    @Lob
    private byte[] data;

    private Long fileSize;
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize =  fileSize;
    }

    @ManyToOne
    @JoinColumn(name="noteId")
    private Note note;

    public Fajl(){}
    public Fajl(String fileName, String fileType, byte[] data, Note note) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.note = note;
    }

    public Long getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setDataUsingInputStream(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[2048];
        File file = new File(note.getNoteId().toString());
        file.mkdir();
        int length;
        String filePath = file.getAbsoluteFile() + "/" + fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        setFilePath(filePath);
        while ((length =  inputStream.read(bytes)) != -1){
            fileOutputStream.write(bytes, 0, length);
        }
        inputStream.close();
        fileOutputStream.close();
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

}
