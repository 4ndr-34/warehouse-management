package com.backend.warehouse_management.repository;

import com.backend.warehouse_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from User user where user.email= :email")
    Optional<User> findByEmail(String email);

}
