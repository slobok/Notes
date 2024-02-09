package com.example.notes.repository;

import com.example.notes.data.Fajl;
import com.example.notes.data.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FajlRepository extends JpaRepository<Fajl, Long> {

    List<Fajl> findByNote(Note note);

    Fajl findByFileIdAndNote(Long id,Note note);

}
