package com.secure.notes.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secure.notes.domain.entity.audit.Audit;
import com.secure.notes.domain.entity.audit.AuditListener;
import com.secure.notes.domain.entity.audit.IAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditListener.class)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        }
)
public class User implements IAudit, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @JsonIgnore
    @Size(max = 120)
    private String password;

    @Email
    @NotBlank
    @Size(max = 50)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @JsonBackReference
    @ToString.Exclude
    private Role role;

    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    private LocalDate credentialsExpiryDate = LocalDate.now().plusYears(1);
    private LocalDate accountExpiryDate = LocalDate.now().plusYears(1);

    private String twoFactorSecret;
    private boolean isTwoFactorEnabled = false;
    private String signUpMethod;

    @Embedded
    @ToString.Exclude
    private Audit audit;

    public User(String userName, String email, String password) {
        this.username = userName;
        this.email = email;
        this.password = password;
    }

    public User(String userName, String email) {
        this.username = userName;
        this.email = email;
    }

}
