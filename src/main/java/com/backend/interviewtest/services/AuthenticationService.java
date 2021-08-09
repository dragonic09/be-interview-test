package com.backend.interviewtest.services;

import com.backend.interviewtest.dto.AuthTokenDto;
import com.backend.interviewtest.dto.UserAccountDto;

public interface AuthenticationService {
    public abstract AuthTokenDto authenticateUserAccount(UserAccountDto userAccount);
}
