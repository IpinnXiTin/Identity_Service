package com.ipinxitin.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ipinxitin.identity_service.dto.request.RoleRequest;
import com.ipinxitin.identity_service.dto.response.RoleResponse;
import com.ipinxitin.identity_service.entity.RoleEntity;
import com.ipinxitin.identity_service.mapper.RoleMapper;
import com.ipinxitin.identity_service.repository.PermissionRepository;
import com.ipinxitin.identity_service.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService {
    
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        
        RoleEntity roleEntity = roleMapper.toRoleEntity(request);
        
        var permissions = permissionRepository.findAllById(request.getPermissions());
        roleEntity.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(roleEntity));
    }

    public List<RoleResponse> getRoles() {
        return roleRepository.findAll()
            .stream().map(roleMapper::toRoleResponse)
            .toList();
    }

    public String deleteRole(String roleName) {
        roleRepository.deleteById(roleName);
        return roleName;
    }
}
