package com.ipinxitin.identity_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ipinxitin.identity_service.dto.request.PermissionRequest;
import com.ipinxitin.identity_service.dto.response.PermissionResponse;
import com.ipinxitin.identity_service.entity.PermissionEntity;
import com.ipinxitin.identity_service.mapper.PermissionMapper;
import com.ipinxitin.identity_service.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionService {
    
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        PermissionEntity permissionEntity = permissionMapper.toPermissionEntity(request);

        return permissionMapper.toPermissionResponse(permissionRepository.save(permissionEntity));
    }

    public List<PermissionResponse> getPermissions() {
        return permissionRepository.findAll()
            .stream().map(permissionMapper::toPermissionResponse)
            .toList();
    }

    public String deletePermission(String permissionName) {
        permissionRepository.deleteById(permissionName);
        return permissionName;
    }
}
