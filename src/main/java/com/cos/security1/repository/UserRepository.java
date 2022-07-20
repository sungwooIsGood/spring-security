package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<User,Integer> 쓰면
// @Repository라는 어노테이션 없어도 IOC된다.
public interface UserRepository extends JpaRepository<User,Integer> {
    // findBy 규칙=> Username문법
    // select * from user where username = ?호출(String username)
    public User findByUsername(String username);
}
