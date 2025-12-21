package com.secure.notes.web.resources;

import com.secure.notes.domain.entity.AuditLog;
import com.secure.notes.domain.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.secure.notes.common.constants.UrlConstant.AUDIT_LOG_URL;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AUDIT_LOG_URL)
public class AuditLogResource {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogsByUsername() {
        return ResponseEntity.ok(auditLogService.getAllAuditLogs());
    }

    @GetMapping("/note/{id}")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs(@PathVariable Long id) {
        return ResponseEntity.ok(auditLogService.getAllAuditLogsByNote(id));
    }


}
