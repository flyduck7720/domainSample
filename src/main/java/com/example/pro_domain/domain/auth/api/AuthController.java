package com.example.pro_domain.domain.auth.api;

import com.example.pro_domain.domain.auth.application.AuthService;
import com.example.pro_domain.domain.auth.dto.SignInReq;
import com.example.pro_domain.domain.auth.dto.SignUpReq;
import com.example.pro_domain.domain.auth.dto.SignUpRes;
import com.example.pro_domain.global.config.jwt.dto.RegenerateTokenDto;
import com.example.pro_domain.global.config.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  //회원가입
  @PostMapping("/signUp")
  public SignUpRes signUp(@Validated SignUpReq signUpReq) {
    log.info("++builder++ = " + SignUpReq.builder());
    return authService.signUp(signUpReq);
  }

  //로그인
  @PostMapping("/signIn")
  public ResponseEntity<TokenDto> signIn(@Validated SignInReq signInReq) {
    return authService.signIn(signInReq);
  }

  @PostMapping("/regenerateToken")
  public ResponseEntity<TokenDto> regenerateToken(@Validated RegenerateTokenDto refreshTokenDto) {
    return authService.regenerateToken(refreshTokenDto);
  }
}
