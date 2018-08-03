package com.lynn.lynn.models.User;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;
    private String password;
    private String passwordConfirm;
    private String role = "USER";

    public User() {}

    public User(String aUsername, String aEmail, String aPassword) {
        this.username = aUsername;
        this.email = aEmail;
        this.password = hashedPassword(aPassword);
    }

    //http://www.appsdeveloperblog.com/encrypt-user-password-example-java/
    private String hashedPassword(String password) {
        String salt = PasswordUtils.getSalt(32);
        String hashedPassword = PasswordUtils.generateSecurePassword(password,salt);
        return hashedPassword + "," + salt;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
