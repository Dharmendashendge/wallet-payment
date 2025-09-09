package com.example.wallet.repository;

import com.example.wallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmailId(String emailId);

    Optional<User> findByUsername(String username);
}