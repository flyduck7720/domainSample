package com.example.pro_domain.domain.user.application;

import com.example.pro_domain.domain.user.domain.User;
import com.example.pro_domain.domain.user.exception.UserNotFoundException;
import com.example.pro_domain.domain.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UserNotFoundException {
    log.info("userId in loadUserByUsername = " + userId);
    //System.out.println("email in loadUserByUsername = " + email);
    //User user = userRepository.findByEmail(email)
    //    .orElseThrow(UserNotFoundException::new);

    User user = userRepository.findByUserId(userId)
        .orElseThrow(UserNotFoundException::new);

    log.info("++user++ = " + user);

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

    log.info("getUserId() = " + user.getUserId());
    log.info("getPassword() = " + user.getPassword());
    log.info("grantedAuthorities  = " + grantedAuthorities);

    return new org
        .springframework
        .security
        .core
        .userdetails
        .User(user.getUserId(), user.getPassword(), grantedAuthorities);
  }
}