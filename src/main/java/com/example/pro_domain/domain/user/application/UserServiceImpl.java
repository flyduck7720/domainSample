package com.example.pro_domain.domain.user.application;

import com.example.pro_domain.domain.user.domain.User;
import com.example.pro_domain.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public Optional<User> findByUserId(String userId) {
    return userRepository.findByUserId(userId);
  }



  @Override
  public Optional<User> findByName(String name) {
    return userRepository.findByName(name);
  }

  @Override
  public User updateUser(User user, String newInfo) {
    return null;
  }
}
