package com.secure.notes.web.resources;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.domain.service.UserService;
import com.secure.notes.web.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.secure.notes.common.constants.UrlConstant.ADMIN_URL;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ADMIN_URL)
public class AdminResource {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<AppRole[]> getAllRoles() {
        return ResponseEntity.ok(AppRole.values());
    }

    @PutMapping("/update-role")
    public ResponseEntity<String> updateUserRole(
            @RequestParam Long userId,
            @RequestParam AppRole appRole
    ) {
        userService.updateUserRole(userId, appRole);
        return ResponseEntity.ok(appRole.name());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

}
