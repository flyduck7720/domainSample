package com.example.pro_domain.domain.user.api;

import com.example.pro_domain.domain.user.application.UserService;
import com.example.pro_domain.domain.user.application.UserService;
import com.example.pro_domain.domain.user.domain.User;
import com.example.pro_domain.domain.user.dto.ProfileDto;
import com.example.pro_domain.domain.user.dto.ProfileDto.ProfileRes;
import com.example.pro_domain.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User 관련 HTTP 요청 처리
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/profile")
  public ProfileDto.ProfileRes profile(@AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
    System.out.println("userDetails = " + userDetails);
    User userDetail = userService.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new UserNotFoundException());

    return ProfileDto.ProfileRes.builder()
        .email(userDetail.getEmail())
        .name(userDetail.getName())
        .build();
  }

  @GetMapping("/profile/view/{username}")
  public ProfileRes userProfile(@PathVariable String username) throws UserNotFoundException {
    User user = userService.findByName(username)
        .orElseThrow(UserNotFoundException::new);

    return ProfileRes.builder()
        .email(user.getEmail())
        .name(user.getName())
        .build();
  }

  @GetMapping("/userList")
  public List<User> showUserList() {
    return userService.findAll();
  }
}
