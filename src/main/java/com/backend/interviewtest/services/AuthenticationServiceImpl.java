package com.backend.interviewtest.services;

import com.backend.interviewtest.dto.AuthTokenDto;
import com.backend.interviewtest.dto.UserAccountDto;
import com.backend.interviewtest.entities.AccessToken;
import com.backend.interviewtest.entities.UserAccount;
import com.backend.interviewtest.repositories.AccessTokenRepository;
import com.backend.interviewtest.repositories.UserAccountRepository;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserAccountRepository userAccountRepository;
    private final HashingService hashingService;
    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    AuthenticationServiceImpl(
            UserAccountRepository userAccountRepository,
            HashingService hashingService,
            AccessTokenRepository accessTokenRepository
    ) {
        this.userAccountRepository = userAccountRepository;
        this.hashingService = hashingService;
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public AuthTokenDto authenticateUserAccount(UserAccountDto userAccount) {
        UserAccount userAccountEntity = this.userAccountRepository.findByUserIdAndPassword(userAccount.getUserId(), userAccount.getPassword());

        if (Objects.isNull(userAccountEntity)) {
            return null;
        }

        String token = this.hashingService.hashString(userAccountEntity.getUserId());

        AccessToken accessTokenEntity = new AccessToken();
        accessTokenEntity.setAccessToken(token);
        accessTokenEntity.setPublishAt(new Date());
        this.accessTokenRepository.save(accessTokenEntity);

        AuthTokenDto authToken = new AuthTokenDto();
        authToken.setAccessToken(token);
        authToken.setTokenType("bearer");
        return authToken;
    }
}
