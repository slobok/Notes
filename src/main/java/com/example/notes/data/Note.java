package com.example.notes.data;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "note_id")
    private Long noteId;
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

    @ManyToMany(mappedBy = "labeledNotes",cascade = CascadeType.ALL)
    private List<Label> label = new ArrayList<>();

    public Note(){}
    public Note(String title, String text, Long createdByUser, String noteColor) {
        this.title = title;
        this.text = text;
        this.createdByUser = createdByUser;
        this.setNoteColor(noteColor);
        setPinned(false);
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
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

    public List<Label> getLabel() {
        return label;
    }

    public void setLabel(List<Label> label) {
        this.label.addAll(label);
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