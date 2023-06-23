package com.example.pro_domain.global.common;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    public void prePersist() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    @LastModifiedDate
    @Column(updatable = false)
    private LocalDateTime modifiedDate;

    @PreUpdate
    public void preUpdate () {
        if (modifiedDate == null) {
            modifiedDate = LocalDateTime.now();
        }
    }
}