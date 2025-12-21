package com.secure.notes.domain.repository;

import com.secure.notes.domain.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByOwnerUsername(String ownerUsername);

    @Query("SELECT n FROM Note n WHERE n.id = :noteId")
    Optional<Note> findByNoteId(@Param("noteId") Long noteId);

    List<Note> findAllByOwnerUsername(String ownerUsername);

}
