package com.example.pro_domain.domain.user.application;


import com.example.pro_domain.domain.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
  /**
   * 모든 유저 리스트를 반환
   * @return 유저 리스트
   */
  List<User> findAll();

  /**
   * 이메일을 통해 유저 조회
   * @param email
   * @return 조회된 유저
   */
  Optional<User> findByEmail(String email);

  Optional<User> findByUserId(String userId);

  /**
   * 이름을 통해 유저 조회
   * @param name
   * @return 조회된 유저
   */
  Optional<User> findByName(String name);

//  /**
//   * Security Context에 존재하는 인증 정보를 통해 유저 정보 조회
//   * @return 조회된 유저
//   */
//  Optional<User> getMyInfo();

  /**
   * 유저 정보 수정
   * @param user 수정활 User Entity
   * @param newInfo
   * @return 수정된 User
   */
  User updateUser(User user, String newInfo);
}
