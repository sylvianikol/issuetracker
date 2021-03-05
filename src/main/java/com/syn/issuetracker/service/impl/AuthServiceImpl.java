package com.syn.issuetracker.service.impl;

import com.auth0.jwt.JWT;
import com.syn.issuetracker.exception.UnprocessableEntityException;
import com.syn.issuetracker.payload.request.LoginRequest;
import com.syn.issuetracker.payload.request.SignUpRequest;
import com.syn.issuetracker.payload.response.JwtResponse;
import com.syn.issuetracker.security.UserDetailsImpl;
import com.syn.issuetracker.service.AuthService;
import com.syn.issuetracker.service.UserService;
import com.syn.issuetracker.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.syn.issuetracker.common.ExceptionErrorMessages.VALIDATION_FAILED;
import static com.syn.issuetracker.common.SecurityConstants.EXPIRATION_TIME;
import static com.syn.issuetracker.common.SecurityConstants.SECRET;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ValidationUtil validationUtil;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserService userService, ValidationUtil validationUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.validationUtil = validationUtil;
    }

    @Override
    public JwtResponse register(SignUpRequest signUpRequest) {

        if (!this.validationUtil.isValid(signUpRequest)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED);
        }

        LoginRequest loginRequest = this.userService.register(signUpRequest);

        return this.login(loginRequest);
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {

        if (!this.validationUtil.isValid(loginRequest)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

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
                roles,
                expirationDate);
    }
}
