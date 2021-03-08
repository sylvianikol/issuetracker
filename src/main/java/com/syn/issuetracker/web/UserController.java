package com.syn.issuetracker.web;

import com.syn.issuetracker.exception.error.ErrorResponse;
import com.syn.issuetracker.model.binding.UserEditBindingModel;
import com.syn.issuetracker.model.service.UserServiceModel;
import com.syn.issuetracker.model.view.UserViewModel;
import com.syn.issuetracker.service.UserService;
import com.syn.issuetracker.specification.UserSpecification;
import com.syn.issuetracker.utils.BindingResultErrorExtractor;
import com.syn.issuetracker.utils.ErrorExtractor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;
import static com.syn.issuetracker.common.MiscConstants.TOTAL_ITEMS;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private ErrorExtractor<BindingResult, String> errorExtractor;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.errorExtractor = new BindingResultErrorExtractor();
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam(required = false, name = "username") String username,
                                                     @PageableDefault(sort = {"username"},
                                                             direction = Sort.Direction.ASC) Pageable pageable) {

        Map<String, Object> response = this.userService
                .getAll(new UserSpecification(username), pageable);

        return (long) response.get(TOTAL_ITEMS) == 0L
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", NO_USERS_FOUND))
                : ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserViewModel> get(@PathVariable String userId) {

        Optional<UserServiceModel> user =
                this.userService.get(userId);

        return user.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(user.get(), UserViewModel.class));

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{userId}")
    public ResponseEntity<?> edit(@PathVariable String userId,
                                  @Valid @RequestBody UserEditBindingModel userEditBindingModel,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(422, VALIDATION_FAILURE,
                            this.errorExtractor.extract(bindingResult)));
        }

        this.userService.edit(userEditBindingModel, userId);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable String userId) {

        this.userService.delete(userId);

        return ResponseEntity.ok().build();
    }
}
