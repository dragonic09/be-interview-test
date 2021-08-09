package com.backend.interviewtest.controllers;

import com.backend.interviewtest.dto.AuthTokenDto;
import com.backend.interviewtest.dto.UserAccountDto;
import com.backend.interviewtest.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<AuthTokenDto> login(@RequestBody UserAccountDto userAccount) {
        AuthTokenDto token = this.authenticationService.authenticateUserAccount(userAccount);
        if (Objects.isNull(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        return ResponseEntity.ok()
                .body(token);
    }

}
