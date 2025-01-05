package com.ipinxitin.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ipinxitin.identity_service.dto.request.UserCreationRequest;
import com.ipinxitin.identity_service.dto.request.UserUpdateRequest;
import com.ipinxitin.identity_service.dto.response.UserResponse;
import com.ipinxitin.identity_service.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(UserEntity userEntity);

    void updateUser(@MappingTarget UserEntity userEntity, UserUpdateRequest request);

    @Mapping(target = "roles", ignore = true)
    UserEntity toUserEntity(UserCreationRequest request);
}