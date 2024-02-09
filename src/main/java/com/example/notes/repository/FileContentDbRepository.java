package com.example.notes.repository;

import com.example.notes.data.FileContentDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileContentDbRepository extends JpaRepository<FileContentDb, Long> {

}
