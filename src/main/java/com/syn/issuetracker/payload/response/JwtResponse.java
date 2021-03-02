package com.syn.issuetracker.payload.response;

import java.util.Date;
import java.util.List;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private String message;
    private List<String> roles;
    private Date expirationDate;

    public JwtResponse(String accessToken, String id, String username, String email, String message, List<String> roles, Date expirationDate) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.message = message;
        this.roles = roles;
        this.expirationDate = expirationDate;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
