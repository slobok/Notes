package com.example.Notes.Repository;

import com.example.Notes.Data.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoteRepository extends JpaRepository <Note, Long > {

}
