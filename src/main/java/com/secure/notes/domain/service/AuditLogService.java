package com.secure.notes.domain.service;

import com.secure.notes.domain.entity.AuditLog;
import com.secure.notes.domain.entity.Note;

import java.util.List;

public interface AuditLogService {

    void logNoteCreation(String username, Note note);

    void logNoteUpdate(String username, Note note);

    void logNoteDeletion(String username, Note note);

    List<AuditLog> getAllAuditLogs();

    List<AuditLog> getAllAuditLogsByUser(String username);

    List<AuditLog> getAllAuditLogsByNote(Long noteId);

}
