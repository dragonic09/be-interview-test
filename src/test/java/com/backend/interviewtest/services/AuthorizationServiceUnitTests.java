package com.backend.interviewtest.services;

import com.backend.interviewtest.dto.UserAccountDto;
import com.backend.interviewtest.entities.AccessToken;
import com.backend.interviewtest.entities.UserAccount;
import com.backend.interviewtest.repositories.AccessTokenRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceUnitTests {
    @Mock
    private AccessTokenRepository accessTokenRepository;

    private AuthorizationService authorizationService;
    private String accessTokenMock = "TestAccessToken";
    @BeforeEach
    public void setup() throws Exception {
        this.authorizationService = new AuthorizationServiceImpl(this.accessTokenRepository);
    }

    @Test
    public void testTokenIsValidIsAuthorizedShouldReturnTrue() throws Exception {
        List<AccessToken> expectedTokenList = new ArrayList();
        AccessToken expectedToken = new AccessToken();
        expectedToken.setAccessToken(this.accessTokenMock);
        expectedToken.setPublishAt(new Date());
        expectedTokenList.add(expectedToken);
        Mockito.when(this.accessTokenRepository.findByAccessToken(this.accessTokenMock))
                .thenReturn(expectedTokenList);
        Boolean result = this.authorizationService.isAuthorized(this.accessTokenMock);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testTokenIsInvalidIsAuthorizedShouldReturnFalse() throws Exception {
        List<AccessToken> expectedTokenList = new ArrayList();
        Mockito.when(this.accessTokenRepository.findByAccessToken(this.accessTokenMock))
                .thenReturn(expectedTokenList);
        Boolean result = this.authorizationService.isAuthorized(this.accessTokenMock);
        Assert.assertEquals(result, false);
    }
}
