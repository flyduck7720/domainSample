package com.example.pro_domain.domain.auth.dto;

import com.example.pro_domain.global.common.dtos.CoreRes;
import lombok.Getter;


@Getter
public class SignUpRes extends CoreRes {
  public SignUpRes(boolean ok, String error) {
    super(ok, error);
  }
}
