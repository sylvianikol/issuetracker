package com.syn.issuetracker.service;

import com.syn.issuetracker.payload.request.LoginRequest;
import com.syn.issuetracker.payload.request.SignUpRequest;
import com.syn.issuetracker.payload.response.JwtResponse;

public interface AuthService {

    JwtResponse register(SignUpRequest signUpRequest);

    JwtResponse login(LoginRequest loginRequest);
}
