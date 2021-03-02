package com.syn.issuetracker.service.impl;

import com.auth0.jwt.JWT;
import com.syn.issuetracker.enums.UserRole;
import com.syn.issuetracker.exception.CustomEntityNotFoundException;
import com.syn.issuetracker.exception.DataConflictException;
import com.syn.issuetracker.payload.request.SignUpRequest;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.service.UserServiceModel;
import com.syn.issuetracker.payload.request.LoginRequest;
import com.syn.issuetracker.payload.response.JwtResponse;
import com.syn.issuetracker.repository.UserRepository;
import com.syn.issuetracker.repository.UserRoleRepository;
import com.syn.issuetracker.security.UserDetailsImpl;
import com.syn.issuetracker.service.UserService;
import com.syn.issuetracker.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.syn.issuetracker.common.ExceptionErrorMessages.*;
import static com.syn.issuetracker.common.ResponseMessages.USER_LOGIN_SUCCESS;
import static com.syn.issuetracker.common.SecurityConstants.EXPIRATION_TIME;
import static com.syn.issuetracker.common.SecurityConstants.SECRET;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, ValidationUtil validationUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtResponse register(SignUpRequest signUpRequest) {

//        if (!this.validationUtil.isValid(signUpRequest)) {
//            throw new UnprocessableEntityException(VALIDATION_FAILED,
//                    this.validationUtil.getViolations(signUpRequest));
//        }

        String email = signUpRequest.getEmail();
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new DataConflictException(EMAIL_ALREADY_EXISTS);
        }

        UserEntity user = this.modelMapper.map(signUpRequest, UserEntity.class);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setAuthorities(List.of(this.userRoleRepository.findByRole(UserRole.USER)));
        this.userRepository.save(user);

        return this.login(new LoginRequest(
                signUpRequest.getEmail(),
                signUpRequest.getPassword())
        );
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(expirationDate)
                .sign(HMAC512(SECRET.getBytes()));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(token,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                USER_LOGIN_SUCCESS,
                roles,
                expirationDate);
    }

    @Override
    public Optional<UserServiceModel> get(String developerId) {
        Optional<UserEntity> developer = this.userRepository.findById(developerId);

        return developer.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(developer.get(), UserServiceModel.class));
    }

    @Override
    public Set<UserServiceModel> getAll() {
        Set<UserServiceModel> developers = this.userRepository
                .findAll().stream()
                .map(d -> this.modelMapper.map(d, UserServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(developers);
    }

    @Override
    public UserServiceModel edit(SignUpRequest signUpRequest, String developerId) {

        UserEntity userEntity = this.userRepository.findById(developerId)
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(USER_NOT_FOUND); });

//        if (!this.validationUtil.isValid(signUpRequest)) {
//            throw new UnprocessableEntityException(VALIDATION_FAILED,
//                    this.validationUtil.getViolations(signUpRequest));
//        }

        userEntity.setEmail(signUpRequest.getEmail());
        this.userRepository.save(userEntity);

        return this.modelMapper.map(userEntity, UserServiceModel.class);
    }

    @Override
    public void delete(String developerId) {
        UserEntity userEntity = this.userRepository.findById(developerId)
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(USER_NOT_FOUND); });

        this.userRepository.delete(userEntity);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

}
