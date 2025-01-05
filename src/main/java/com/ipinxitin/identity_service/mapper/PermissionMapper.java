package com.ipinxitin.identity_service.mapper;

import org.mapstruct.Mapper;

import com.ipinxitin.identity_service.dto.request.PermissionRequest;
import com.ipinxitin.identity_service.dto.response.PermissionResponse;
import com.ipinxitin.identity_service.entity.PermissionEntity;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toPermissionResponse(PermissionEntity permissionEntity);

    PermissionEntity toPermissionEntity(PermissionRequest permissionRequest);
}
