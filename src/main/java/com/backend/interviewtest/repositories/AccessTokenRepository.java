package com.backend.interviewtest.repositories;

import com.backend.interviewtest.entities.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, Long> {
    List<AccessToken> findByAccessToken(String accessToken);
}
