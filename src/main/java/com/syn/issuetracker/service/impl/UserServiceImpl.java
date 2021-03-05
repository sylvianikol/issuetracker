package com.syn.issuetracker.service.impl;

import com.syn.issuetracker.enums.UserRole;
import com.syn.issuetracker.exception.CustomEntityNotFoundException;
import com.syn.issuetracker.exception.DataConflictException;
import com.syn.issuetracker.exception.UnprocessableEntityException;
import com.syn.issuetracker.payload.request.SignUpRequest;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.service.UserServiceModel;
import com.syn.issuetracker.payload.request.LoginRequest;
import com.syn.issuetracker.repository.UserRepository;
import com.syn.issuetracker.repository.UserRoleRepository;
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

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public LoginRequest register(SignUpRequest signUpRequest) {

        String email = signUpRequest.getEmail();
        if (this.userRepository.findByUsername(email).isPresent()) {
            throw new DataConflictException(EMAIL_ALREADY_EXISTS);
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
        List<UserEntity> users = usersPage.getContent();

//        Set<UserServiceModel> users = this.userRepository
//                .findAll().stream()
//                .map(d -> this.modelMapper.map(d, UserServiceModel.class))
//                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<String, Object> response = new HashMap<>();
        response.put("users", Collections.unmodifiableList(users));
        response.put("currentPage", usersPage.getNumber());
        response.put("totalItems", usersPage.getTotalElements());
        response.put("totalPages", usersPage.getTotalPages());

        return response;
    }

    @Override
    public UserServiceModel edit(SignUpRequest signUpRequest, String developerId) {

        UserEntity userEntity = this.userRepository.findById(developerId)
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(USER_NOT_FOUND); });

        if (!this.validationUtil.isValid(signUpRequest)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED);
        }

        userEntity.setEmail(signUpRequest.getEmail());
        this.userRepository.save(userEntity);

        return this.modelMapper.map(userEntity, UserServiceModel.class);
    }

    @Override
    public void delete(String developerId) {

        UserEntity user = this.userRepository.findById(developerId)
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(USER_NOT_FOUND); });

        this.userRepository.delete(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return this.userRepository.findByUsername(email);
    }

    @Override
    public boolean isAdmin(String userId) {
        return this.userRepository.getIfAdmin(userId).isPresent();
    }

}
