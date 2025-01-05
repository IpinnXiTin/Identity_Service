package com.ipinxitin.identity_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ipinxitin.identity_service.dto.request.AuthenticationRequest;
import com.ipinxitin.identity_service.dto.request.IntrospectRequest;
import com.ipinxitin.identity_service.dto.request.LogoutRequest;
import com.ipinxitin.identity_service.dto.request.RefreshRequest;
import com.ipinxitin.identity_service.dto.response.ApiResponse;
import com.ipinxitin.identity_service.dto.response.AuthenticationResponse;
import com.ipinxitin.identity_service.dto.response.IntrospectResponse;
import com.ipinxitin.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
            .code(1000)
            .message("Login successfully!")
            .result(authenticationService.authenticate(request))
            .build();
    }
    
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) 
            throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
            .code(1000)
            .message("Verified token!")
            .result(authenticationService.introspect(request))
            .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) 
            throws ParseException, JOSEException {

        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Token has been logged out successfully!")
            .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) 
            throws ParseException, JOSEException {

        var result = authenticationService.refreshToken(request);

        return ApiResponse.<AuthenticationResponse>builder()
            .code(1000)
            .message("Token has been refresh successfully!")
            .result(result)
            .build();
    }
}
