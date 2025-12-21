package com.secure.notes.domain.entity;

import com.secure.notes.domain.entity.audit.Audit;
import com.secure.notes.domain.entity.audit.AuditListener;
import com.secure.notes.domain.entity.audit.IAudit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "authorities")
@EntityListeners(AuditListener.class)
public class Authority implements IAudit, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    @ManyToOne(
            targetEntity = User.class
    )
    @JoinColumn(
            name = "username",
            referencedColumnName = "username"
    )
    private User user;

    @Embedded
    @ToString.Exclude
    private Audit audit;

}
