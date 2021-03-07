package com.syn.issuetracker.service;

import com.syn.issuetracker.model.payload.request.LoginRequest;
import com.syn.issuetracker.model.payload.request.SignUpRequest;
import com.syn.issuetracker.model.payload.response.JwtResponse;

public interface AuthService {

    JwtResponse register(SignUpRequest signUpRequest);

    JwtResponse login(LoginRequest loginRequest);
}
