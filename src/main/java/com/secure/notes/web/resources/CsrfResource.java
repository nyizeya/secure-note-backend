package com.secure.notes.web.resources;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.secure.notes.common.constants.UrlConstant.CSRF_URL;

@Slf4j
@RestController
@RequestMapping(CSRF_URL)
public class CsrfResource {

    @GetMapping
    public ResponseEntity<CsrfToken> getCsrfToken(HttpServletRequest request) {
        return ResponseEntity.ok((CsrfToken) request.getAttribute(CsrfToken.class.getName()));
    }

}
