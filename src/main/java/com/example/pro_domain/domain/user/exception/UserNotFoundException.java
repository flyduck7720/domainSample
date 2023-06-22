package com.example.pro_domain.domain.user.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("Can't find User");
  }

}
