package com.syn.issuetracker.web;

import com.syn.issuetracker.payload.request.SignUpRequest;
import com.syn.issuetracker.payload.request.LoginRequest;
import com.syn.issuetracker.payload.response.JwtResponse;
import com.syn.issuetracker.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest,
                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(signUpRequest);
        }

        JwtResponse jwtResponse = this.authService.register(signUpRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(loginRequest);
        }

        JwtResponse jwtResponse = this.authService.login(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }
}
