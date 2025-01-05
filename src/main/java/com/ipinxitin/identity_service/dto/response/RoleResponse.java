package com.ipinxitin.identity_service.dto.response;

import java.util.Set;

import com.ipinxitin.identity_service.entity.PermissionEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String roleName;
    String description;
    Set<PermissionEntity> permissions;
}
