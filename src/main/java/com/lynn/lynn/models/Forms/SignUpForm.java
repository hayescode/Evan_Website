package com.lynn.lynn.models.Forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class SignUpForm {

    @Size(min = 1, message = "Enter a username")
    private String username;

    @Email
    @Size(min = 1, message = "Enter an email address")
    private String email;

    @Size(min = 1, message = "Enter password")
    private String password;

    @Size(min = 1, message = "Passwords must match")
    private String verify;

    public SignUpForm() {}

    public SignUpForm(String aUsername, String aEmail, String aPassword, String aVerify) {
        this.username = aUsername;
        this.email = aEmail;
        this.password = aPassword;
        this.verify = aVerify;
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

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
