package com.secure.notes.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secure.notes.domain.entity.audit.Audit;
import com.secure.notes.domain.entity.audit.AuditListener;
import com.secure.notes.domain.entity.audit.IAudit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditListener.class)
public class PasswordResetToken implements IAudit, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    private boolean used = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User user;

    @Embedded
    @JsonIgnore
    @ToString.Exclude
    private Audit audit;

    public PasswordResetToken(User user, String token, Instant expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
