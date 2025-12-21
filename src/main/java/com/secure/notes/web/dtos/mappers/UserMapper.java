package com.secure.notes.web.dtos.mappers;

import com.secure.notes.common.enumerations.AppRole;
import com.secure.notes.domain.entity.Role;
import com.secure.notes.domain.entity.User;
import com.secure.notes.web.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserDTO, User> {

    @Mapping(source = "role", target = "role", qualifiedByName = "stringToRole")
    @Mapping(target = "audit", ignore = true)
    @Override
    User toEntity(UserDTO dto);

    @Mapping(source = "role", target = "role", qualifiedByName = "roleToString")
    @Override
    UserDTO toDTO(User entity);

    @Mapping(source = "role", target = "role", qualifiedByName = "stringToRole")
    @Override
    List<User> toEntities(List<UserDTO> dtoList);

    @Mapping(source = "role", target = "role", qualifiedByName = "roleToString")
    @Override
    List<UserDTO> toDTOList(List<User> entityList);

    @Named("roleToString")
    default String mapRoleToString(Role role) {
        return role != null ? role.getRoleName().name() : null;
    }

    @Named("stringToRole")
    default Role stringToRole(String roleName) {
        if (roleName == null) {
            return null;
        }

        return new Role(AppRole.fromName(roleName));
    }
}
