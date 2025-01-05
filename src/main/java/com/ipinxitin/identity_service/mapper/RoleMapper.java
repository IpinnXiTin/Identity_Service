package com.ipinxitin.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ipinxitin.identity_service.dto.request.RoleRequest;
import com.ipinxitin.identity_service.dto.response.RoleResponse;
import com.ipinxitin.identity_service.entity.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(RoleEntity roleEntity);

    @Mapping(target = "permissions", ignore = true)
    RoleEntity toRoleEntity(RoleRequest roleRequest);
}
