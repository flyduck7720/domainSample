package com.example.pro_domain.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class SignInReq {
  @NotEmpty(message = "아이디를 입력해 주세요!")
  private String userId;

  @NotEmpty(message = "비밀번호를 입력해 주세요!")
  private String password;

  @Builder
  public SignInReq(String userId, String password) {
   // this.userId = userId;
    this.userId = userId;
    this.password = password;
  }
}

