package com.example.notes.data;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "label_id")
    private Long labelId;

    @Column(unique=true)
    private String name;
    private Long userId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "note_Label",
            joinColumns = @JoinColumn(name = "label_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    private   List<Note> labeledNotes = new ArrayList<>();

    public List<Note> getLabeledNote() {
        return labeledNotes;
    }

    public void setLabeledNote(List<Note> labeledNote) {
        this.labeledNotes = labeledNote;
    }

    public Label(){}
    public Label(String name) {
        this.name = name;
    }

    public Label(String name,Long userId){
        this.name = name;
        this.userId = userId;
    }

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Note> getLabeledNotes() {
        return labeledNotes;
    }

    public void setLabeledNotes(List<Note> labeledNotes) {
        this.labeledNotes = labeledNotes;
    }

    @Override
    public String toString() {
        return "Label{" +
                "id=" + labelId +
                ", name='" + name + '\'' +
                '}';
    }
}