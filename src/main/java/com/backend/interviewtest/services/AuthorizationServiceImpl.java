package com.backend.interviewtest.services;

import com.backend.interviewtest.entities.AccessToken;
import com.backend.interviewtest.repositories.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService{
    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    AuthorizationServiceImpl(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public Boolean isAuthorized(String token) {
        List<AccessToken> accessTokens =  this.accessTokenRepository.findByAccessToken(token);
        if(accessTokens.size() > 0) {
            return true;
        }
        return false;
    }
}
