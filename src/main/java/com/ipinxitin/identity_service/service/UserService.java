package com.ipinxitin.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ipinxitin.identity_service.dto.request.UserCreationRequest;
import com.ipinxitin.identity_service.dto.request.UserUpdateRequest;
import com.ipinxitin.identity_service.dto.response.UserResponse;
import com.ipinxitin.identity_service.entity.UserEntity;
import com.ipinxitin.identity_service.exception.AppException;
import com.ipinxitin.identity_service.exception.ErrorCode;
import com.ipinxitin.identity_service.mapper.UserMapper;
import com.ipinxitin.identity_service.repository.RoleRepository;
import com.ipinxitin.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {

    RoleRepository roleRepository;
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {

        if (userRepository.existsByUserName(request.getUserName())) 
            throw new AppException(ErrorCode.USER_EXISTED);

        UserEntity user = userMapper.toUserEntity(request);
        var roles = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(roles));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassWord(passwordEncoder.encode(request.getPassWord()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse myInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 
        var user = userRepository.findByUserName(authentication.getName())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PostAuthorize("returnObject.userName == authentication.name")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @PostAuthorize("returnObject.userName == authentication.name")
    public UserResponse updateUser(UserUpdateRequest request, String id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassWord(passwordEncoder.encode(request.getPassWord()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
