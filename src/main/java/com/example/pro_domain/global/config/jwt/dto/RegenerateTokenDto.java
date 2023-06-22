package com.example.pro_domain.global.config.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegenerateTokenDto {
  private String refresh_token;
}
