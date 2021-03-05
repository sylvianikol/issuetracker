package com.syn.issuetracker.web;

import com.syn.issuetracker.payload.request.SignUpRequest;
import com.syn.issuetracker.model.service.UserServiceModel;
import com.syn.issuetracker.model.view.UserViewModel;
import com.syn.issuetracker.service.UserService;
import com.syn.issuetracker.specification.UserSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
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

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam(required = false, name = "username") String username,
                                                     @PageableDefault(sort = {"username"},
                                                             direction = Sort.Direction.ASC) Pageable pageable) {

        Map<String, Object> users = this.userService
                .getAll(new UserSpecification(username), pageable);
//
        return users.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(users);
    }

    // TODO: pre-authorize USER with userId and ADMIN
//    @PreAuthorize("hasRole('ROLE_ADMIN') or #authUser.id == #userId")
    @GetMapping("/{userId}")
    public ResponseEntity<UserViewModel> get(@PathVariable String userId
//                                             @AuthenticationPrincipal UserDetailsImpl authUser
    ) {

        Optional<UserServiceModel> developer =
                this.userService.get(userId);

        return developer.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(developer.get(), UserViewModel.class));

    }

    // TODO: pre-authorize USER with userId and ADMIN
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

    // TODO: pre-authorize USER with userId and ADMIN
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable String userId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.userService.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path("/users").build().toUri())
                .build();
    }
}
