package com.roydevelop.helloworld.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roydevelop.helloworld.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
