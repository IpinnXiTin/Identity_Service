package com.ipinxitin.identity_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipinxitin.identity_service.dto.request.UserCreationRequest;
import com.ipinxitin.identity_service.dto.request.UserUpdateRequest;
import com.ipinxitin.identity_service.dto.response.ApiResponse;
import com.ipinxitin.identity_service.dto.response.UserResponse;
import com.ipinxitin.identity_service.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {

        return ApiResponse.<UserResponse>builder()
            .code(1000)
            .message("User has been created successfully!")
            .result(userService.createUser(request))
            .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {

        return ApiResponse.<List<UserResponse>>builder()
            .code(1000)
            .message("User list has been retrieved successfully!")
            .result(userService.getUsers())
            .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> myInfo() {

        return ApiResponse.<UserResponse>builder()
            .code(1000)
            .message("Current user has been retrieved successfully!")
            .result(userService.myInfo())
            .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {

        return ApiResponse.<UserResponse>builder()
            .code(1000)
            .message("User has been retrieved successfully by id!")
            .result(userService.getUser(id))
            .build();
    }
    
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request, @PathVariable String id) {

        return ApiResponse.<UserResponse>builder()
            .code(1000)
            .message("User has been updated successfully!")
            .result(userService.updateUser(request, id))
            .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable String id) {

        userService.deleteUser(id);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("User has been deleted successfully!")
            .result(id)
            .build();
    } 
}
