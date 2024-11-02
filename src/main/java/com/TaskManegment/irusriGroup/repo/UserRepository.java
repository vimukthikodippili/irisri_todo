package com.TaskManegment.irusriGroup.repo;


import com.TaskManegment.irusriGroup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(String email);


    Optional<User> findByEmail(String email);
}
