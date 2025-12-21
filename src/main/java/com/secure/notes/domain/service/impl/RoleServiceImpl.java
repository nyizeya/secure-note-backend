package com.secure.notes.domain.service.impl;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.common.exceptions.InvalidOperationException;
import com.secure.notes.domain.entity.Role;
import com.secure.notes.domain.repository.RoleRepository;
import com.secure.notes.domain.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public Role createNewRole(AppRole appRole) {
        Optional<Role> roleOpt = roleRepository.findByRoleName(appRole);

        if (roleOpt.isPresent()) throw new InvalidOperationException("Role [%s] already exists".formatted(roleOpt.get().getRoleName()));

        return roleRepository.save(new Role(appRole));

    }

    @Override
    public Optional<Role> findByRoleName(AppRole role) {
        return roleRepository.findByRoleName(role);
    }

    @Override
    public boolean existsByRoleName(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
