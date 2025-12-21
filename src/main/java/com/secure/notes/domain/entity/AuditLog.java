package com.secure.notes.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secure.notes.common.enumerations.AuditLogType;
import com.secure.notes.domain.entity.audit.Audit;
import com.secure.notes.domain.entity.audit.AuditListener;
import com.secure.notes.domain.entity.audit.IAudit;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditListener.class)
public class AuditLog implements IAudit, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AuditLogType action;

    private String username;
    private Long noteId;
    private String noteContent;
    private LocalDateTime timestamp = LocalDateTime.now();

    @Embedded
    @JsonIgnore
    @ToString.Exclude
    private Audit audit;

    public AuditLog(Long noteId, AuditLogType action, String username, String noteContent) {
        this.noteId = noteId;
        this.action = action;
        this.username = username;
        this.noteContent = noteContent;
    }
}
