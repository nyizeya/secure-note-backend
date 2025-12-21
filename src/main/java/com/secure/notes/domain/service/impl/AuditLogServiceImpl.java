package com.secure.notes.domain.service.impl;

import com.secure.notes.common.enumerations.AuditLogType;
import com.secure.notes.domain.entity.AuditLog;
import com.secure.notes.domain.entity.Note;
import com.secure.notes.domain.repository.AuditLogRepository;
import com.secure.notes.domain.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Even though all methods of this class need Transaction for operations, I did not use @Transactional annotation.
     * That's because the invoking methods have already contained @Transactional and these methods can use existing Transaction.
     */

    @Override
    public void logNoteCreation(String username, Note note) {
        log.info("Auditing {} note", AuditLogType.CREATE.name());
        auditLogRepository.save(new AuditLog(note.getId(), AuditLogType.CREATE, username, note.getContent()));
    }

    @Override
    public void logNoteUpdate(String username, Note note) {
        log.info("Auditing {} note", AuditLogType.UPDATE.name());
        auditLogRepository.save(new AuditLog(note.getId(), AuditLogType.UPDATE, username, note.getContent()));
    }

    @Override
    public void logNoteDeletion(String username, Note note) {
        log.info("Auditing {} note", AuditLogType.DELETE.name());
        auditLogRepository.save(new AuditLog(note.getId(), AuditLogType.DELETE, username, note.getContent()));
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    @Override
    public List<AuditLog> getAllAuditLogsByUser(String username) {
        return auditLogRepository.findAllByUsername(username);
    }

    @Override
    public List<AuditLog> getAllAuditLogsByNote(Long noteId) {
        return auditLogRepository.findAllByNoteId(noteId);
    }
}
