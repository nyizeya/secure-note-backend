package com.secure.notes.web.resources;

import com.secure.notes.domain.entity.Note;
import com.secure.notes.domain.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.secure.notes.common.constants.UrlConstant.NOTE_URL;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(NOTE_URL)
public class NoteResource {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<Note> createNote(
            @RequestBody String content,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNoteForUser(userDetails.getUsername(), content));
    }

    @GetMapping
    public ResponseEntity<List<Note>> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("UserDetails: {}", userDetails.getUsername());
        return ResponseEntity.ok(noteService.getNotesForUser(userDetails.getUsername()));
    }

    @PutMapping("/{noteId}/update")
    public ResponseEntity<Note> updateNote(
            @PathVariable Long noteId,
            @RequestBody String content,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(noteService.updateNoteForUser(noteId, userDetails.getUsername(), content));
    }

    @DeleteMapping("/{noteId}/delete")
    public ResponseEntity<?> delete(
            @PathVariable Long noteId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        noteService.deleteNoteForUser(noteId, userDetails.getUsername());
        return ResponseEntity.ok(noteId);
    }

}
