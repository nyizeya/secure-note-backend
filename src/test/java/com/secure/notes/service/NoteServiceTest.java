package com.secure.notes.service;

import com.secure.notes.domain.entity.Note;
import com.secure.notes.domain.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NoteServiceTest {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void testCreateNote() {
        Note note = new Note();
        note.setOwnerUsername("john");
        note.setContent("Spring Security OAuth2 Fullstack Note Application");

        note = noteRepository.save(note);
        System.out.println(note);
    }

}
