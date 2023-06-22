package com.example.pro_domain.domain.user.application;

import com.example.pro_domain.domain.user.domain.User;
import com.example.pro_domain.domain.user.exception.UserNotFoundException;
import com.example.pro_domain.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
    System.out.println("email in loadUserByUsername = " + email);
    User user = userRepository.findByEmail(email)
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