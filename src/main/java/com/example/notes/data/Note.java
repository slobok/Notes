package com.example.notes.data;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    private int isTrashed;
    private int isArchived;
    private boolean isCheckBoxForm;
    private String textCheckBoxIndex;
    private boolean pinned;
    private Long createdByUser;
    private String noteColor;


    @ManyToMany(mappedBy = "labeledNotes")
    Set<Label> label = new HashSet<>();

    public Note(){}
    public Note(String title, String text, Long createdByUser) {
        this.title = title;
        this.text = text;
        this.createdByUser = createdByUser;
        this.setNoteColor("#FFFAF0");
        setPinned(false);
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

    public int getIsTrashed() {
        return isTrashed;
    }

    public void setIsTrashed(int isTrashed) {
        this.isTrashed = isTrashed;
    }

    public int getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(int isArchived) {
        this.isArchived = isArchived;
    }

    public boolean isCheckBoxForm() {
        return isCheckBoxForm;
    }

    public void setCheckBoxForm(boolean checkBoxForm) {
        isCheckBoxForm = checkBoxForm;
    }

    public String getTextCheckBoxIndex() {
        return textCheckBoxIndex;
    }

    public void setTextCheckBoxIndex(String textCheckBoxIndex) {
        this.textCheckBoxIndex = textCheckBoxIndex;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
    public void changePinned(){
        this.pinned = !this.pinned;
    }

    public Set<Label> getLabel() {
        return label;
    }

    public void setLabel(Set<Label> label) {
        this.label = label;
    }
    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
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