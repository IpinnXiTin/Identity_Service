package com.ipinxitin.identity_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ipinxitin.identity_service.dto.request.RoleRequest;
import com.ipinxitin.identity_service.dto.response.ApiResponse;
import com.ipinxitin.identity_service.dto.response.RoleResponse;
import com.ipinxitin.identity_service.service.RoleService;

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
@RequestMapping("/roles")
public class RoleController {
    
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        
        return ApiResponse.<RoleResponse>builder()
            .code(1000)
            .message("Role has been created successfully!")
            .result(roleService.createRole(request))
            .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getRoles() {
        
        return ApiResponse.<List<RoleResponse>>builder()
            .code(1000)
            .message("Role list has been retrieved successfully!")
            .result(roleService.getRoles())
            .build();
    }
    
    @DeleteMapping("/{role}")
    public ApiResponse<String> deleteRole(@PathVariable String role) {
        
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Role has been deleted successfully!")
            .result(roleService.deleteRole(role))
            .build();
    }
}
