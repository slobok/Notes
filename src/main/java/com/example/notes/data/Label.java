package com.example.notes.data;

import jakarta.persistence.*;

import java.util.Set;

@Table
@Entity
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //Ne moze da postoji label sa istim imenom za jednog korisnika
    @Column(unique=true)
    private String name;
    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "note_Label",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))

    Set<Note> labeledNotes;

    public Set<Note> getLabeledNote() {
        return labeledNotes;
    }

    public void setLabeledNote(Set<Note> labeledNote) {
        this.labeledNotes = labeledNote;
    }

    public Label(){};
    public Label(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Note> getLabeledNotes() {
        return labeledNotes;
    }

    public void setLabeledNotes(Set<Note> labeledNotes) {
        this.labeledNotes = labeledNotes;
    }

    @Override
    public String toString() {
        return "Label{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
