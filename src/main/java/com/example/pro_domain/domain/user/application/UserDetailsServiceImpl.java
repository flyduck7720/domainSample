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
  public UserDetails loadUserByUsername(String memEmail) throws UserNotFoundException {
    log.info("email in loadUserByUsername = " + memEmail);
    //System.out.println("email in loadUserByUsername = " + email);
    User user = userRepository.findByEmail(memEmail)
        .orElseThrow(UserNotFoundException::new);
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

    return new org
        .springframework
        .security
        .core
        .userdetails
        .User(user.getEmail(), user.getPassword(), grantedAuthorities);
  }
}