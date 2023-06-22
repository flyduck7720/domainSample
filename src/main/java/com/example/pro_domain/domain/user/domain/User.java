package com.example.pro_domain.domain.user.domain;

import com.example.pro_domain.global.common.domain.CoreEntity;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;

// @Entity 어노테이션을 클래스에 선언하면 그 클래스는 JPA가 관리
@Entity
@Getter @Setter
@NoArgsConstructor
@ToString
public class User extends CoreEntity {
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String password;
  @Column(length = 10, nullable = false, unique = true)
  private String name;

//  @Enumerated(EnumType.STRING)
//  private UserRole role;

  @Builder
  public User(String email, String password, String name /*UserRole role*/) {
    this.email = email;
    this.password = password;
    this.name = name;
//    this.role = role;
  }

  // https://reflectoring.io/spring-security-password-handling/
  /**
   * 비밀번호를 암호화
   * @param passwordEncoder 암호화 할 인코더 클래스
   * @return 변경된 유저 Entity
   */
  public User hashPassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(this.password);
    return this;
  }
}
