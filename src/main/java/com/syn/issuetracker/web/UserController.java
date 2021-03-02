package com.syn.issuetracker.web;

import com.syn.issuetracker.payload.request.SignUpRequest;
import com.syn.issuetracker.model.service.UserServiceModel;
import com.syn.issuetracker.model.view.UserViewModel;
import com.syn.issuetracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin
    @GetMapping("")
    public ResponseEntity<Set<UserViewModel>> getAll() {

        Set<UserViewModel> developers = this.userService.getAll().stream()
                .map(r -> this.modelMapper.map(r, UserViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return developers.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(developers);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<UserViewModel> get(@PathVariable String id) {

        Optional<UserServiceModel> developer =
                this.userService.get(id);

        return developer.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(developer.get(), UserViewModel.class));

    }

    @CrossOrigin
    @PutMapping("/{userId}")
    public ResponseEntity<?> edit(@PathVariable String userId,
                                  @Valid @RequestBody SignUpRequest signUpRequest,
                                  BindingResult bindingResult,
                                  UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(signUpRequest);
        }

        UserServiceModel user = this.userService.edit(signUpRequest, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path("/users/{userId}")
                        .buildAndExpand(user.getId())
                        .toUri())
                .build();
    }

    @CrossOrigin
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable String userId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.userService.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path("/users").build().toUri())
                .build();
    }
}
