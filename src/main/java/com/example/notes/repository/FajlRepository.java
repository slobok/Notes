package com.example.notes.repository;

import com.example.notes.data.Fajl;
import com.example.notes.data.FileContentDb;
import com.example.notes.data.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface FajlRepository extends JpaRepository<Fajl, Long> {

    List<Fajl> findByNote(Note note);

}
