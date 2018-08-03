package com.lynn.lynn.models.Data;


import com.lynn.lynn.models.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
}