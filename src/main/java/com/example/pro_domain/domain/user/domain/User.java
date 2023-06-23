package com.example.pro_domain.domain.user.domain;

import com.example.pro_domain.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

// @Entity 어노테이션을 클래스에 선언하면 그 클래스는 JPA가 관리
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Table(name = "MEMBER", indexes = @Index(name = "idx__email__birthday", columnList = "email, birthday"))  //인덱스키 설정

/*
@Table(indexes = {
        @Index(name="idx_book_name", columnList = "name"),
        @Index(name="idx_book_name_published_date", columnList = "name, publishedDateOn"),
        @Index(name="unique_idx_book_name_author", columnList = "name, author", unique = true)
})
*/
@Table(name = "TB_MEMBER", indexes = @Index(name = "idx_memId", columnList = "MEM_ID", unique = true))

public class User extends BaseTimeEntity {

  @Id //PK
  @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY 전략은 기본 키 생성을 프로젝트에 연결된 데이터베이스에 위임하는 전략
  private long idx;

  @Column(length = 30, nullable = false, unique = true, name = "MEM_ID")
  private String userId;

  @Column(length = 30, nullable = false, name = "MEM_NAME")
  private String name;

  @Column(length = 50, nullable = false, name = "MEM_EMAIL")
  private String email;

  @Column(nullable = false, name = "MEM_PWD")
  private String password;

  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING) //DB는 RoleType에 대한 정보를 모르기에 각 Enum 이름(String)을 컬럼에 저장
  private UserRole role;


  @Builder
  public User(String userId, String email, String password, String name, UserRole role) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.name = name;
    this.role = role;
  }

  // https://reflectoring.io/spring-security-password-handling/
  /**
   * 비밀번호를 암호화
   * @param passwordEncoder 암호화 할 인코더 클래스
   * @return 변경된 유저 Entity
   */
  public User hashPassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(this.password);
    return this;
  }


  public User update(String name){
    this.name = name;

    return this;
  }

}
