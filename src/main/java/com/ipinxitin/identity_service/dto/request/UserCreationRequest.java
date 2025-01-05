package com.ipinxitin.identity_service.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.ipinxitin.identity_service.validator.DobConstraint;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "INVALID_USERNAME")
    String userName;

    @Size(min = 8, message = "INVALID_PASSWORD")
    String passWord;
    String firstName;
    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DATE_OF_BIRTH")
    LocalDate dob;

    Set<String> roles;
}
