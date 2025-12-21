package com.secure.notes.domain.repository;

import com.secure.notes.domain.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("SELECT a FROM AuditLog a WHERE a.username = :username")
    List<AuditLog> findAllByUsername(String username);

    List<AuditLog> findAllByNoteId(Long noteId);

}
