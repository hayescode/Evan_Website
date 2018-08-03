package com.lynn.lynn.Security;


import com.lynn.lynn.models.User.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
