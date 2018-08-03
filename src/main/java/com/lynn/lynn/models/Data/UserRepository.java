package com.lynn.lynn.models.Data;

import com.lynn.lynn.models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}