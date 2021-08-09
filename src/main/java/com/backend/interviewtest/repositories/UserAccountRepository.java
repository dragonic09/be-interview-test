package com.backend.interviewtest.repositories;

import com.backend.interviewtest.entities.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    UserAccount findByUserIdAndPassword(String userId, String password);
}
