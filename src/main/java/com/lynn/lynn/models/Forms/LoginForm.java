package com.lynn.lynn.models.Forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginForm {
    @Size(min = 1, message = "Must enter a username")
    @NotNull
    private String username;

    @NotNull
    @Size(min = 1, message = "Must enter a password")
    private String password;

    public LoginForm() {}

    public LoginForm(String aUsername, String aPassword) {
        this.username = aUsername;
        this.password = aPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
