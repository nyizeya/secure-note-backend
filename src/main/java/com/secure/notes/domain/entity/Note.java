package com.secure.notes.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secure.notes.domain.entity.audit.Audit;
import com.secure.notes.domain.entity.audit.AuditListener;
import com.secure.notes.domain.entity.audit.IAudit;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notes")
@EntityListeners(AuditListener.class)
public class Note implements IAudit, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private String ownerUsername;

    @Embedded
    @JsonIgnore
    @ToString.Exclude
    private Audit audit;

    public Note(String ownerUsername, String content) {
        this.ownerUsername = ownerUsername;
        this.content = content;
    }
}
