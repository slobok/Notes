package com.example.Notes.Repository;

import com.example.Notes.Data.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository <Note, Long > {
    @Query("SELECT n FROM Note n WHERE lower(n.title) like lower(concat('%', :searchTerm , '%'))" +
            "or lower(n.text) like lower(concat('%', :searchTerm , '%'))")
    List<Note> search(@Param("searchTerm") String search);

}
