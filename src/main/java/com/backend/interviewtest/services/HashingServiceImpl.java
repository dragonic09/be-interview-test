package com.backend.interviewtest.services;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
// abstract 3rd party dependencies to make it easier to test
@Service
public class HashingServiceImpl implements HashingService {
    HashingServiceImpl() {}

    @Override
    public String hashString(String targetString) {
        return Hashing.sha256()
                .hashString(targetString, StandardCharsets.UTF_8)
                .toString();
    }
}
