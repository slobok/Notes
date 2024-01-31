package com.example.notes.data;

import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name="noteId")
    private Note note;

    public Fajl(){}
    public Fajl(String fileName, String fileType, byte[] data, Note note) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data   = data;
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

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

}
