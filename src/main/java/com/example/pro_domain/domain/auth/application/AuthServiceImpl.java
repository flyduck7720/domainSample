package com.example.pro_domain.domain.auth.application;

import com.example.pro_domain.domain.user.domain.User;
import com.example.pro_domain.domain.auth.dto.SignInReq;
import com.example.pro_domain.domain.auth.dto.SignUpReq;
import com.example.pro_domain.domain.auth.dto.SignUpRes;
import com.example.pro_domain.domain.user.repository.UserRepository;
import com.example.pro_domain.global.config.jwt.JwtTokenProvider;
import com.example.pro_domain.global.config.jwt.dto.RegenerateTokenDto;
import com.example.pro_domain.global.config.jwt.dto.TokenDto;
import com.example.pro_domain.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.pro_domain.global.config.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder bCryptPasswordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;
  private final RedisTemplate<String, String> redisTemplate;


  @Override
  @Transactional
  public SignUpRes signUp(SignUpReq signUpReq){
    log.info("signUpReq = " + signUpReq.toString());
    //System.out.println("signUpReq = " + signUpReq.toString());

    if(userRepository.existsByEmail(signUpReq.getUserId())) {
      return new SignUpRes(false, "Your Mail already Exist.");
    }
    User newUser = signUpReq.toUserEntity();
    newUser.hashPassword(bCryptPasswordEncoder);

    User user = userRepository.save(newUser);
    if(!Objects.isNull(user)) {
      return new SignUpRes(true, null);
    }
    return new SignUpRes(false, "Fail to Sign Up");
  }

  @Override
  public ResponseEntity<TokenDto> signIn(SignInReq signInReq) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              signInReq.getUserId(),
              signInReq.getPassword()
          )
      );

      String refresh_token = jwtTokenProvider.createRefreshToken(authentication);

      TokenDto tokenDto = new TokenDto(
          jwtTokenProvider.createAccessToken(authentication),
          refresh_token
      );

      // Redis에 저장 - 만료 시간 설정을 통해 자동 삭제 처리
      redisTemplate.opsForValue().set(
              authentication.getName(),
              refresh_token,
              REFRESH_TOKEN_EXPIRATION_TIME.getValue(),
              TimeUnit.MILLISECONDS
          );

      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("Authorization", "Bearer " + tokenDto.getAccess_token());

      return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid credentials supplied", HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity<TokenDto> regenerateToken(RegenerateTokenDto refreshTokenDto) {
    String refresh_token = refreshTokenDto.getRefresh_token();
    try {
      // Refresh Token 검증
      if (!jwtTokenProvider.validateRefreshToken(refresh_token)) {
        throw new CustomException("Invalid refresh token supplied", HttpStatus.BAD_REQUEST);
      }

      // Access Token 에서 User email를 가져온다.
      Authentication authentication = jwtTokenProvider.getAuthentication(refresh_token);

      // Redis에서 저장된 Refresh Token 값을 가져온다.
      String refreshToken = redisTemplate.opsForValue().get(authentication.getName());
      if(!refreshToken.equals(refresh_token)) {
        throw new CustomException("Refresh Token doesn't match.", HttpStatus.BAD_REQUEST);
      }

      // 토큰 재발행
      String new_refresh_token = jwtTokenProvider.createRefreshToken(authentication);
      TokenDto tokenDto = new TokenDto(
          jwtTokenProvider.createAccessToken(authentication),
          new_refresh_token
      );

      // RefreshToken Redis에 업데이트
      redisTemplate.opsForValue().set(
          authentication.getName(),
          new_refresh_token,
          REFRESH_TOKEN_EXPIRATION_TIME.getValue(),
          TimeUnit.MILLISECONDS
      );

      HttpHeaders httpHeaders = new HttpHeaders();

      return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid refresh token supplied", HttpStatus.BAD_REQUEST);
    }
  }
}
