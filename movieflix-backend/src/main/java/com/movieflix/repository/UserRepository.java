package com.movieflix.repository;

import com.movieflix.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Find user by username (for authentication)
    Optional<User> findByUsername(String username);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find active users
    List<User> findByIsActiveTrue();

    // Find users by role
    List<User> findByRolesContaining(String role);

    // Find admin users
    // List<User> findByRolesContaining(String role);
}
