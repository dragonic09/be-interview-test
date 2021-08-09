package com.backend.interviewtest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthTokenDto {
    private String accessToken;
    private String tokenType;
}
