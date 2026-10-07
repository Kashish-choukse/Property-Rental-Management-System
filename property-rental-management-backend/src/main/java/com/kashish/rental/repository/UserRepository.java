package com.kashish.rental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kashish.rental.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}