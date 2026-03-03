package edu.iu.habahram.ducksservice.model;

public class SigninRequest {
    private String username;
    private String password;

    public SigninRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}