package com.lynn.lynn.Security;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}