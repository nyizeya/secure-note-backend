package com.secure.notes.web.resources;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.domain.entity.User;
import com.secure.notes.domain.service.RoleService;
import com.secure.notes.domain.service.UserService;
import com.secure.notes.security.jwt.services.JwtService;
import com.secure.notes.web.dtos.LoginResponseDTO;
import com.secure.notes.web.dtos.MessageResponseDTO;
import com.secure.notes.web.dtos.request.LoginRequest;
import com.secure.notes.web.dtos.request.ResetPasswordRequest;
import com.secure.notes.web.dtos.request.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.secure.notes.common.constants.UrlConstant.AUTH_URL;

@Slf4j
@RestController
@RequestMapping(AUTH_URL)
@RequiredArgsConstructor
public class AuthorizationResource {

    private final JwtService jwtService;
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Bad Credentials"));
        }

        // really important to set Authentication, so the Framework knows current user is Authenticated
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String jwtToken = jwtService.generateTokenFromUsername(userDetails);
        return ResponseEntity.ok(new LoginResponseDTO(userDetails.getUsername(), jwtToken, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
        if (userService.existsByUsername(request.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error username already exists"));

        if (userService.existsByEmail(request.getEmail()))
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error email already exists"));

        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());
        user.setSignUpMethod("email");
        user.setRole(roleService.findByRoleName(AppRole.ROLE_USER).get());

        return ResponseEntity.ok(userService.createNewUser(user));
    }

    @PostMapping("/forgot-password")
    public CompletableFuture<ResponseEntity<MessageResponseDTO>> forgotPassword(@RequestParam String email) {
        log.info("Generate password reset token...");
        return userService.generatePasswordResetToken(email).thenApply((isSent) ->
                isSent ? ResponseEntity.ok(new MessageResponseDTO("Password reset email was sent.")) :
                        ResponseEntity.internalServerError().body(new MessageResponseDTO("Error sending password reset email")));
    };

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        try {
            userService.resetPassword(request);
            return ResponseEntity.ok(new MessageResponseDTO("Password has been reset successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error reset password : " + e.getMessage()));
        }
    }

    @PostMapping("/verify-2fa-login")
    public ResponseEntity<?> verify2FALogin(
            @RequestParam int code,
            @RequestParam String jwtToken
    ) {
        String username = jwtService.getUsernameFromToken(jwtToken);
        User user = userService.getUserByUsername(username);
        boolean isValid = userService.validate2FACode(user.getId(), code);

        if (isValid) {
            return ResponseEntity.ok("2FA is verified.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid 2FA code");
    }

}
