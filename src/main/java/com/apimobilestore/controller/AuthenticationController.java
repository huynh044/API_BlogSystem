package com.apimobilestore.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apimobilestore.dto.request.AuthenticationRequest;
import com.apimobilestore.dto.request.IntrospectRequest;
import com.apimobilestore.dto.request.LogoutRequest;
import com.apimobilestore.dto.request.RefreshRequest;
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.AuthenticationResponse;
import com.apimobilestore.dto.response.IntrospectResponse;
import com.apimobilestore.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService service;
    static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request){
        logger.info("Login request received for user: {}", request.getUsername());
        AuthenticationResponse result = service.Authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
	
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request) throws ParseException, JOSEException{
        logger.info("Introspect request received for token: {}", request.getToken());
        IntrospectResponse result = service.introspectResponse(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
	
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshRequest request) throws ParseException, JOSEException{
        logger.info("Refresh token request received for token: {}", request.getToken());
        AuthenticationResponse result = service.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
	
    @PostMapping("/logout")
    ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request) throws ParseException, JOSEException {
        logger.info("Logout request received for token: {}", request.getToken());
        service.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}
