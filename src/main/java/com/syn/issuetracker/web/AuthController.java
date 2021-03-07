package com.syn.issuetracker.web;

import com.syn.issuetracker.exception.error.ErrorResponse;
import com.syn.issuetracker.model.payload.request.SignUpRequest;
import com.syn.issuetracker.model.payload.request.LoginRequest;
import com.syn.issuetracker.model.payload.response.JwtResponse;
import com.syn.issuetracker.service.AuthService;
import com.syn.issuetracker.utils.BindingResultErrorExtractor;
import com.syn.issuetracker.utils.ErrorExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.syn.issuetracker.common.ExceptionErrorMessages.VALIDATION_FAILURE;

@CrossOrigin
@RestController
public class AuthController {

    private final AuthService authService;
    private ErrorExtractor<BindingResult, String> errorExtractor;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
        this.errorExtractor = new BindingResultErrorExtractor();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest,
                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(422, VALIDATION_FAILURE,
                            this.errorExtractor.extract(bindingResult)));
        }

        JwtResponse jwtResponse = this.authService.register(signUpRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest loginRequest,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(422, VALIDATION_FAILURE,
                            this.errorExtractor.extract(bindingResult)));
        }

        JwtResponse jwtResponse = this.authService.login(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }
}
