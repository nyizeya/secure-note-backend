package com.secure.notes.domain.service.impl;

import com.secure.notes.common.exceptions.InvalidOperationException;
import com.secure.notes.common.utils.CommonUtils;
import com.secure.notes.domain.entity.Note;
import com.secure.notes.domain.repository.NoteRepository;
import com.secure.notes.domain.service.AuditLogService;
import com.secure.notes.domain.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final AuditLogService auditLogService;

    @Transactional
    @Override
    public Note createNoteForUser(String username, String content) {
        log.info("Creating note for user {}", username);
        Note note = new Note(username, content);
        note =  noteRepository.save(note);
        auditLogService.logNoteCreation(username, note);
        return note;
    }

    @Transactional
    @Override
    public Note updateNoteForUser(Long noteId, String username, String content) {
        log.info("Updating note [{}] for user {}", noteId, username);
        Note note = noteRepository.findById(noteId).orElseThrow(() -> CommonUtils.createEntityNotFoundException("Note", "id", noteId));

        if (!username.equals(note.getOwnerUsername())) throw new InvalidOperationException("Invalid note update for logged in user");

        note.setContent(content);
        auditLogService.logNoteUpdate(username, note);
        return noteRepository.save(note);
    }

    @Transactional
    @Override
    public void deleteNoteForUser(Long noteId, String username) {
        log.info("Deleting note [{}] for user {}", noteId, username);

        noteRepository.findByNoteId(noteId).ifPresent(note -> {
            if (!username.equals(note.getOwnerUsername())) throw new InvalidOperationException("Invalid note update for logged in user");
            auditLogService.logNoteDeletion(username, note);
            noteRepository.delete(note);
        });

    }

    @Transactional
    @Override
    public List<Note> getNotesForUser(String username) {
        return noteRepository.findAllByOwnerUsername(username);
    }
}
