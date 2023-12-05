package com.example.Notes.Repository;

import com.example.Notes.Data.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository <Note, Long > {

}
