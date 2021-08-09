package com.backend.interviewtest.services;

import com.backend.interviewtest.dto.AuthTokenDto;
import com.backend.interviewtest.dto.UserAccountDto;
import com.backend.interviewtest.entities.AccessToken;
import com.backend.interviewtest.entities.UserAccount;
import com.backend.interviewtest.repositories.AccessTokenRepository;
import com.backend.interviewtest.repositories.UserAccountRepository;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceUnitTests {
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private HashingService hashingService;
    @Mock
    private AccessTokenRepository accessTokenRepository;

    private AuthenticationService authenticationService;
    private UserAccountDto userAccountMock;
    private UserAccount userAccountEntityMock;

    @BeforeEach
    public void setup() throws Exception {
        this.authenticationService = new AuthenticationServiceImpl(
                this.userAccountRepository,
                this.hashingService,
                this.accessTokenRepository
        );
        String userId = "testId";
        String password = "password";

        this.userAccountMock = new UserAccountDto();
        this.userAccountMock.setUserId(userId);
        this.userAccountMock.setPassword(password);

        this.userAccountEntityMock = new UserAccount();
        this.userAccountEntityMock.setUserId(userId);
        this.userAccountEntityMock.setPassword(password);
    }

    @Test
    public void testAuthenticateUserWhenUserAccountIsCorrectShouldReturnAuthToken() throws Exception {
        this.userAccountEntityMock.setUserId(this.userAccountMock.getUserId());
        this.userAccountEntityMock.setPassword(this.userAccountMock.getPassword());

        String expectedToken = "TestHash";
        String expectedTokenType = "bearer";

        Mockito.when(this.userAccountRepository.findByUserIdAndPassword(this.userAccountMock.getUserId(), this.userAccountMock.getPassword()))
                .thenReturn(this.userAccountEntityMock);
        Mockito.when(this.hashingService.hashString(this.userAccountMock.getUserId())).thenReturn(expectedToken);
        AuthTokenDto resultToken = this.authenticationService.authenticateUserAccount(this.userAccountMock);
        Assert.assertEquals(resultToken.getAccessToken(), expectedToken);
        Assert.assertEquals(resultToken.getTokenType(), expectedTokenType);
    }

    @Test
    public void testAuthenticateUserWhenUserAccountIsCorrectShouldSaveToken() throws Exception {
        this.userAccountEntityMock.setUserId(this.userAccountMock.getUserId());
        this.userAccountEntityMock.setPassword(this.userAccountMock.getPassword());

        String expectedToken = "TestHash";
        Date expectedPublishAt = new Date();

        AccessToken expectedAccessToken = new AccessToken();
        expectedAccessToken.setId(Integer.toUnsignedLong(1));
        expectedAccessToken.setAccessToken(expectedToken);
        expectedAccessToken.setPublishAt(expectedPublishAt);

        Mockito.when(this.userAccountRepository.findByUserIdAndPassword(this.userAccountMock.getUserId(), this.userAccountMock.getPassword()))
                .thenReturn(this.userAccountEntityMock);
        Mockito.when(this.hashingService.hashString(this.userAccountMock.getUserId())).thenReturn(expectedToken);
        Mockito.when(this.accessTokenRepository.save(Mockito.any(AccessToken.class))).thenReturn(expectedAccessToken);
        this.authenticationService.authenticateUserAccount(this.userAccountMock);
        Mockito.verify(this.accessTokenRepository, Mockito.times(1)).save(Mockito.any(AccessToken.class));
    }

    @Test
    public void testAuthenticateUserWhenUserAccountIsInCorrectShouldReturnNull() throws Exception {
        Mockito.when(this.userAccountRepository.findByUserIdAndPassword(this.userAccountMock.getUserId(), this.userAccountMock.getPassword()))
                .thenReturn(null);
        AuthTokenDto resultToken = this.authenticationService.authenticateUserAccount(this.userAccountMock);
        Assert.assertEquals(resultToken, null);
    }
}
