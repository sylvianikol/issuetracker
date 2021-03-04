package com.syn.issuetracker.service;

import com.syn.issuetracker.payload.request.SignUpRequest;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.service.UserServiceModel;
import com.syn.issuetracker.payload.request.LoginRequest;
import com.syn.issuetracker.payload.response.JwtResponse;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    JwtResponse register(SignUpRequest signUpRequest);

    JwtResponse login(LoginRequest loginRequest);

    Optional<UserServiceModel> get(String userId);

    Set<UserServiceModel> getAll();

    UserServiceModel edit(SignUpRequest signUpRequest, String userId);

    void delete(String userId);

    Optional<UserEntity> findByEmail(String email);

    boolean isAdmin(String userId);

}
