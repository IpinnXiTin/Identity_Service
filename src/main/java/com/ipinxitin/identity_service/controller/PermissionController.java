package com.ipinxitin.identity_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ipinxitin.identity_service.dto.request.PermissionRequest;
import com.ipinxitin.identity_service.dto.response.ApiResponse;
import com.ipinxitin.identity_service.dto.response.PermissionResponse;
import com.ipinxitin.identity_service.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionController {
    
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        
        return ApiResponse.<PermissionResponse>builder()
            .code(1000)
            .message("Permission has been created successfully!")
            .result(permissionService.createPermission(request))
            .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getPermissions() {
        
        return ApiResponse.<List<PermissionResponse>>builder()
            .code(1000)
            .message("Permission list has been retrieved successfully!")
            .result(permissionService.getPermissions())
            .build();
    }
    
    @DeleteMapping("/{permission}")
    public ApiResponse<String> deletePermission(@PathVariable String permission) {
        
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Permission has been deleted successfully!")
            .result(permissionService.deletePermission(permission))
            .build();
    }
}
