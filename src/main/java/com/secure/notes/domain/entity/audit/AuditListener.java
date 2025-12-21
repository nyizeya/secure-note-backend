package com.secure.notes.domain.entity.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.util.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

public class AuditListener {

    @PrePersist
    public void setCreated(Object object) {
        if (!(object instanceof IAudit)) return;

        IAudit iAudit = (IAudit) object;
        Audit audit = iAudit.getAudit();

        if (ObjectUtils.isEmpty(audit)) {
            audit = new Audit();
            iAudit.setAudit(audit);
        }

        audit.setCreatedAt(new Date());
        audit.setCreatedBy(getUsername());
    }

    @PreUpdate
    public void updatedAt(Object object) {
        if (!(object instanceof IAudit)) return;

        IAudit iAudit = (IAudit) object;
        Audit audit = iAudit.getAudit();

        if (ObjectUtils.isEmpty(audit)) {
            audit = new Audit();
            iAudit.setAudit(audit);
        }

        audit.setModifiedAt(new Date());
        audit.setModifiedBy(getUsername());
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ObjectUtils.isEmpty(authentication) ? "System" : authentication.getName();
    }

}
