package com.example.pro_domain.domain.user.dto;

import lombok.Builder;
import lombok.Data;

public class ProfileDto {
  @Data
  @Builder
  public static class ProfileReq {
    private String name;
  }

  @Data
  @Builder
  public static class ProfileRes {
    private String email;
    private String name;
  }
}
