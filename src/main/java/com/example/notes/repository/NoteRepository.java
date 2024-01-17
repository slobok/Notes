package com.example.notes.repository;

import com.example.notes.data.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long > {

    List<Note> findByIsTrashed(int isTrashed);
    List<Note> findByIsArchived(int isArchived);
    List<Note> findByIsTrashedAndIsArchived(int isTrashed, int isArchived);
    int countByIsTrashed(int isTrashed);
    int countByIsTrashedAndIsArchived(int isTrashed, int isArchived);

    @Query(value = "SELECT n FROM Note n limit :selectedSamples offset :firstIndex"
            , nativeQuery = true)
    List<Note> myQuery(@Param("firstIndex") Integer offset, @Param("selectedSamples") Integer limit);


    @Query("SELECT n FROM Note n WHERE n.isTrashed = :trash AND n.isArchived = :archive AND (lower(n.title) like lower(concat('%', :searchTerm , '%'))" +
            "or lower(n.text) like lower(concat('%', :searchTerm , '%')))")
    List<Note> search(@Param("searchTerm") String search,
                      @Param("trash")int trash,
                      @Param("archive")int archive);

}