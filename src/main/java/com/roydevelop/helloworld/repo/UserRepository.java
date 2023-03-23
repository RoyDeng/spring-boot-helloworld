package com.roydevelop.helloworld.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.roydevelop.helloworld.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT * FROM users WHERE (username LIKE '%#{query}%' OR email LIKE '%#{query}%')")
    Page<User> findByUserNameOrEmail(@Param("query") String q, Pageable pageable);

    Slice<User> findAllBySlice(Pageable page);
}
