package com.example.pro_domain.domain.user.repository;

import com.example.pro_domain.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, UserRepositoryQuerydsl{

  Optional<User> findByEmail(String email);

  Optional<User> findByName(String name);

  Optional<User> findByUserId(String userId);

  /**
   * 이메일 중복 여부를 확인
   *
   * @param email
   * @return true | false
   */
  boolean existsByEmail(String email);

}
