package com.secure.notes.domain.service;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.domain.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    Role createNewRole(AppRole appRole);

    Optional<Role> findByRoleName(AppRole role);

    List<Role> findAllRoles();

    boolean existsByRoleName(String roleName);

}
