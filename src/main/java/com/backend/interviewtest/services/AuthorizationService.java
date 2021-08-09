package com.backend.interviewtest.services;

public interface AuthorizationService {
    Boolean isAuthorized(String token);
}
