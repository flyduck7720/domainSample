package com.example.pro_domain.domain.auth.dto;

import com.example.pro_domain.domain.user.domain.User;
import com.example.pro_domain.domain.user.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import static com.example.pro_domain.domain.user.domain.QUser.user;

@Log4j2
@Getter
@ToString
public class SignUpReq {

  @NotEmpty(message = "아이디를 입력해 주세요!")
  private String userId;

  @NotEmpty(message = "Please enter your Email")
  @Email
  private String email;

  @NotEmpty(message = "Please enter your Password")
  private String password;

  @NotEmpty(message = "Please enter your Name")
  private String name;

  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @Builder
  public SignUpReq(String userId, String email, String password, String name, UserRole userRole) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.name = name;
    this.userRole = userRole;

  }

  /**
   * Transform to User Entity
   *
   * @return User Entity
   */
  public User toUserEntity() {
    return User.builder()
        .userId(this.getUserId())
        .email(this.getEmail())
        .password(this.getPassword())
        .name(this.getName())
        .role(userRole.USER)
        .build();
  }
}
