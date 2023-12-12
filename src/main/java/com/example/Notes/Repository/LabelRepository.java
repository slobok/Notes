package com.example.Notes.Repository;

import com.example.Notes.Data.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository <Label,Long> {

}
