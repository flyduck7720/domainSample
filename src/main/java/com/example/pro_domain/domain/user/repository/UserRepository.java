package com.example.pro_domain.domain.user.repository;

import com.example.pro_domain.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByName(String name);

  /**
   * 이메일 중복 여부를 확인
   *
   * @param email
   * @return true | false
   */
  boolean existsByEmail(String email);
}
