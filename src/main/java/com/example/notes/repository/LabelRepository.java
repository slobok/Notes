package com.example.notes.repository;

import com.example.notes.data.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository <Label,Long> {
    List <Label> findByName(String labelName);
}