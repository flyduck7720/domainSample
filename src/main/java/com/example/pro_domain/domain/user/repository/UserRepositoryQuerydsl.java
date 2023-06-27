package com.example.pro_domain.domain.user.repository;

import com.example.pro_domain.domain.user.domain.User;

import java.util.Optional;

public interface  UserRepositoryQuerydsl {
    Optional<User> findByUserId(String userId);
}
