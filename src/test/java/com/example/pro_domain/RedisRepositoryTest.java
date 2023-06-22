package com.example.pro_domain;

import com.example.pro_domain.domain.member.api.Person;
import com.example.pro_domain.domain.member.repository.PersonRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisRepositoryTest {

    @Autowired
    private PersonRedisRepository repo;

    @Test
    void test() {
        Person person = new Person("park", 20);

        //저장
        repo.save(person);

        // 'keyspace:id' 값을 가져옴.
        repo.findById(person.getId());

        // Person Entity의 @RedisHash에 정의되어 있는 keyspace (people)에 속한 키의 갯수를 구함.
        repo.count();

        //삭제
        repo.delete(person);
    }


}
