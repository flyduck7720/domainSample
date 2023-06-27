package com.example.pro_domain.global.config.jwt;

import com.example.pro_domain.domain.user.application.UserDetailsServiceImpl;
import com.example.pro_domain.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.pro_domain.global.config.jwt.JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.example.pro_domain.global.config.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtTokenProvider {

  private final RedisTemplate<String, String> redisTemplate;

  @Value("${jwt.secret}")
  private String SECRET_KEY;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;


  /**
   * Access 토큰 생성
   */
  public String createAccessToken(Authentication authentication){
    Claims claims = Jwts.claims().setSubject(authentication.getName());
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME.getValue());

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .signWith(getSigningKey(SECRET_KEY))
            .compact();
  }

  private Key getSigningKey(String secretKey) {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Refresh 토큰 생성
   */
  public String createRefreshToken(Authentication authentication){
    Claims claims = Jwts.claims().setSubject(authentication.getName());
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME.getValue());

    String refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
            .compact();

    // redis에 저장
    redisTemplate.opsForValue().set(
            authentication.getName(),
            refreshToken,
            REFRESH_TOKEN_EXPIRATION_TIME.getValue(),
            TimeUnit.MILLISECONDS
    );

    return refreshToken;
  }


  /**
   * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체 생성해 Authentication 객체 반환
   */
  public Authentication getAuthentication(String token) throws CustomException {
    String userPrincipal = Jwts.parser().
            setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody().getSubject();
    UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  /**
   * http 헤더로부터 bearer 토큰을 가져옴.
   */
  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }


  /**
   * Access 토큰을 검증
   * @param token
   * @return
   */
  public boolean validateAccessToken(String token) {
    try {
      Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      // MalformedJwtException | ExpiredJwtException | IllegalArgumentException
      throw new CustomException("Error on Access Token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Refresh 토큰을 검증
   * @param token
   * @return
   */
  public boolean validateRefreshToken(String token) {
    try {
      Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      // MalformedJwtException | ExpiredJwtException | IllegalArgumentException
      throw new CustomException("Error on Refresh Token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}

