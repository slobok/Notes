package com.example.notes.data;


import jakarta.persistence.Entity;

import jakarta.persistence.*;

import java.sql.Blob;

@Table
@Entity
public class FileContentDb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "label_id")
    private Long id;

    @Lob
    private Blob data;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "data_id", referencedColumnName = "file_id")
    private Fajl fajl;

    public FileContentDb() {

    }

    public FileContentDb(Blob blob){
        setData(blob);
    }

    public Fajl getFajl() {
        return fajl;
    }

    public void setFajl(Fajl fajl) {
        this.fajl = fajl;
    }

    public Long getId() {
        return id;
    }


    public Blob getData() {
        return data;
    }

    public void setData(Blob data) {
        this.data = data;
    }
}
