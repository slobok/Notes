package com.example.Notes.Data;

import jakarta.persistence.*;

@Table
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String title;
    private String text;
    private int isTrashed;
    private int isArchived;

    private Long createdByUser;

    public Note(){}
    public Note(String title, String text, Long createdByUser) {
        this.title = title;
        this.text = text;
        this.createdByUser = createdByUser;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(Long createdByUser) {
        this.createdByUser = createdByUser;
    }

    public int isTrashed() {
        return isTrashed;
    }

    public void setTrashed(int trashed) {
        isTrashed = trashed;
    }

    public int isArchived() {
        return isArchived;
    }

    public void setArchived(int archived) {
        isArchived = archived;
    }


    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", createdByUser=" + createdByUser +
                '}';
    }
}