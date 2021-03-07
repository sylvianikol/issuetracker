package com.syn.issuetracker.service.impl;

import com.syn.issuetracker.model.enums.UserRole;
import com.syn.issuetracker.exception.CustomEntityNotFoundException;
import com.syn.issuetracker.exception.DataConflictException;
import com.syn.issuetracker.exception.UnprocessableEntityException;
import com.syn.issuetracker.model.binding.UserEditBindingModel;
import com.syn.issuetracker.model.entity.UserRoleEntity;
import com.syn.issuetracker.model.view.UserViewModel;
import com.syn.issuetracker.model.payload.request.SignUpRequest;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.service.UserServiceModel;
import com.syn.issuetracker.model.payload.request.LoginRequest;
import com.syn.issuetracker.repository.UserRepository;
import com.syn.issuetracker.repository.UserRoleRepository;
import com.syn.issuetracker.service.TaskService;
import com.syn.issuetracker.service.UserService;
import com.syn.issuetracker.specification.UserSpecification;
import com.syn.issuetracker.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, TaskService taskService, PasswordEncoder passwordEncoder, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public LoginRequest register(SignUpRequest signUpRequest) {

        if (!this.validationUtil.isValid(signUpRequest)) {
            throw new UnprocessableEntityException(VALIDATION_FAILURE);
        }

        String email = signUpRequest.getEmail();
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new DataConflictException(EMAIL_ALREADY_EXISTS);
        }

        String username = signUpRequest.getUsername();
        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new DataConflictException(USERNAME_ALREADY_EXISTS);
        }

        UserEntity user = this.modelMapper.map(signUpRequest, UserEntity.class);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setAuthorities(List.of(this.userRoleRepository.findByRole(UserRole.ROLE_USER)));
        this.userRepository.save(user);

        return new LoginRequest(
                signUpRequest.getUsername(),
                signUpRequest.getPassword());
    }

    @Override
    public Optional<UserServiceModel> get(String developerId) {

        Optional<UserEntity> user = this.userRepository.findById(developerId);

        return user.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(user.get(), UserServiceModel.class));
    }

    @Override
    public Map<String, Object> getAll(UserSpecification userSpecification, Pageable pageable) {

        Page<UserEntity> usersPage = this.userRepository.findAll(userSpecification, pageable);
        List<UserViewModel> users = usersPage.getContent().stream()
                .map(u -> this.modelMapper.map(u, UserViewModel.class))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("users", Collections.unmodifiableList(users));
        response.put("currentPage", usersPage.getNumber());
        response.put("totalItems", usersPage.getTotalElements());
        response.put("totalPages", usersPage.getTotalPages());

        return response;
    }

    @Override
    public UserServiceModel edit(UserEditBindingModel userEditBindingModel, String userId) {

        if (!this.validationUtil.isValid(userEditBindingModel)) {
            throw new UnprocessableEntityException(VALIDATION_FAILURE);
        }

        UserEntity userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(USER_NOT_FOUND); });

        if (this.isAlreadyUsedUsername(userEditBindingModel.getUsername(), userId)) {
            throw new DataConflictException(USERNAME_ALREADY_EXISTS);
        }

        if (this.isAlreadyUsedEmail(userEditBindingModel.getEmail(), userId)) {
            throw new DataConflictException(EMAIL_ALREADY_EXISTS);
        }

        userEntity.setUsername(userEditBindingModel.getUsername());
        userEntity.setEmail(userEditBindingModel.getEmail());

        List<UserRoleEntity> newAuthorities = getUserRoles(userEditBindingModel);

        if (!newAuthorities.isEmpty()) {
            userEntity.setAuthorities(newAuthorities);
        }

        this.userRepository.save(userEntity);

        return this.modelMapper.map(userEntity, UserServiceModel.class);
    }

    @Override
    public void delete(String userId) {

        UserEntity user = this.userRepository.findById(userId)
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(USER_NOT_FOUND); });

        this.taskService.unassignTasks(user.getId());

        this.userRepository.delete(user);
    }

    @Override
    public Optional<UserEntity> findById(String userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public boolean isAdmin(String userId) {
        return this.userRepository.getIfAdmin(userId).isPresent();
    }

    private boolean isAlreadyUsedEmail(String email, String userId) {
        Optional<UserEntity> foundUser = this.userRepository.findByEmail(email);
        return foundUser.isPresent() && !foundUser.get().getId().equals(userId);
    }

    private boolean isAlreadyUsedUsername(String username, String userId) {
        Optional<UserEntity> foundUser = this.userRepository.findByEmail(username);
        return foundUser.isPresent() && !foundUser.get().getId().equals(userId);
    }

    private List<UserRoleEntity> getUserRoles(UserEditBindingModel userEditBindingModel) {
        List<UserRoleEntity> newAuthorities = new ArrayList<>();
        userEditBindingModel.getAuthorities()
                .forEach(a -> {
                    UserRole role = a.getRole();
                    newAuthorities.add(this.userRoleRepository.findByRole(role));
                });
        return newAuthorities;
    }
}
