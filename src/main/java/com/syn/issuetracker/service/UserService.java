package com.syn.issuetracker.service;

import com.syn.issuetracker.model.binding.UserEditBindingModel;
import com.syn.issuetracker.model.payload.request.SignUpRequest;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.service.UserServiceModel;
import com.syn.issuetracker.model.payload.request.LoginRequest;
import com.syn.issuetracker.specification.UserSpecification;

import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    LoginRequest register(SignUpRequest signUpRequest);

    Optional<UserServiceModel> get(String userId);

    Map<String, Object> getAll(UserSpecification userSpecification, Pageable pageable);

    UserServiceModel edit(UserEditBindingModel userEditBindingModel, String userId);

    void delete(String userId);

    Optional<UserEntity> findById(String userId);

    boolean isAdmin(String userId);

}
