package com.secure.notes.repository;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.domain.entity.Role;
import com.secure.notes.domain.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("test_create_user_role")
    void testCreateUserRole() {
        Role role = roleRepository.save(new Role(AppRole.ROLE_USER));
        log.info("Created role => {}", role.getRoleName());

        roleRepository.findByRoleName(AppRole.ROLE_USER).ifPresent(r -> {
            log.info("role => {}", r.getRoleName());
        });
    }

}
