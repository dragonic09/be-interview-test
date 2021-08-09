package com.backend.interviewtest.controllers;

import com.backend.interviewtest.dto.AuthTokenDto;
import com.backend.interviewtest.dto.UserAccountDto;
import com.backend.interviewtest.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerUnitTests {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private UserAccountDto userAccountMock;

    @BeforeEach
    public void setup() {
        this.userAccountMock = new UserAccountDto();
        this.userAccountMock.setUserId("testId");
        this.userAccountMock.setPassword("testPassword");
    }

    @Test
    public void testAuthenticateSuccessfullyShouldReturnTokenWithTokenType() throws Exception {
        AuthTokenDto expectedAuthToken = new AuthTokenDto();
        expectedAuthToken.setAccessToken("TestAccessToken");
        expectedAuthToken.setTokenType("bearer");
        Mockito.when(this.authenticationService.authenticateUserAccount(this.userAccountMock))
                .thenReturn(expectedAuthToken);
        ResponseEntity<AuthTokenDto> response = this.authenticationController.login(this.userAccountMock);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody().getAccessToken(), expectedAuthToken.getAccessToken());
        Assert.assertEquals(response.getBody().getTokenType(), expectedAuthToken.getTokenType());
    }

    @Test
    public void testAuthenticateUserAccountUnsuccessfullyShouldReturnUnauthorized() throws Exception {
        Mockito.when(this.authenticationService.authenticateUserAccount(this.userAccountMock))
                .thenReturn(null);
        ResponseEntity<AuthTokenDto> response = this.authenticationController.login(this.userAccountMock);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assert.assertEquals(response.getBody(), null);
    }
}
