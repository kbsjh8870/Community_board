package org.example.community.repository;

import org.example.community.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    // 아이디, 비번으로 유저 찾기
    Optional<User> findByUsernameAndPassword(String username,String password);
    // 아이디로 유저 찾기
    List<User> findByUsername(String username);
}
