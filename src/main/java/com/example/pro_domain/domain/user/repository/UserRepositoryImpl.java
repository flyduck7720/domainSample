package com.example.pro_domain.domain.user.repository;

import com.example.pro_domain.domain.user.domain.QUser;
import com.example.pro_domain.domain.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryQuerydsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findByUserId(String userId) {
        QUser tb_member = new QUser("tb_member");

        return Optional.ofNullable(queryFactory
                .select(tb_member) // 이름 필드만 선택
                .from(tb_member)
                .where(tb_member.userId.eq(userId))
                .fetchOne()
        );

/*
        return Optional.ofNullable(
                queryFactory
                        .select(
                                Projections.constructor(User.class,
                                        tb_member.email))
                        .from(tb_member)
                        .where(tb_member.userId.eq(userId))
                        .fetchOne()

        );
 */
    }



}
