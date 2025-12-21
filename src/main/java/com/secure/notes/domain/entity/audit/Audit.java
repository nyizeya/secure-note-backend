package com.secure.notes.domain.entity.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.Date;

@Data
@Embeddable
public class Audit {

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "modified_at")
    private Date modifiedAt;

}
