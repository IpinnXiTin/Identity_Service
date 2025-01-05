package com.ipinxitin.identity_service.dto.response;

import java.time.LocalDate;
import java.util.Set;

import com.ipinxitin.identity_service.entity.RoleEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String userName;
    String firstName;
    String lastName;
    LocalDate dob;

    Set<RoleEntity> roles;
}
