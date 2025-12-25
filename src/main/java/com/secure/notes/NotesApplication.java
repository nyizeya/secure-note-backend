package com.secure.notes;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.domain.entity.User;
import com.secure.notes.domain.service.RoleService;
import com.secure.notes.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@SpringBootApplication
@RequiredArgsConstructor
public class NotesApplication {

    private final RoleService roleService;
    private final UserService userService;

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Yangon"));
        SpringApplication.run(NotesApplication.class, args);
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            if (roleService.findAllRoles().isEmpty()) {
                roleService.createNewRole(AppRole.ROLE_ADMIN);
                roleService.createNewRole(AppRole.ROLE_USER);
            }

            if (userService.findAllUsers().isEmpty()) {
                User adminUser = new User("admin", "admin@gmail.com", "1234");
                adminUser.setRole(roleService.findByRoleName(AppRole.ROLE_ADMIN).get());
                userService.createNewUser(adminUser);

                User user = new User("user", "user@gmail.com", "1234");
                user.setRole(roleService.findByRoleName(AppRole.ROLE_USER).get());
                userService.createNewUser(user);
            }
        };
    }

}
