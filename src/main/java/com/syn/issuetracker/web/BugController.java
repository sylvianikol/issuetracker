package com.syn.issuetracker.web;

import com.syn.issuetracker.model.binding.BugAddBindingModel;
import com.syn.issuetracker.model.service.BugServiceModel;
import com.syn.issuetracker.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/bugs")
public class BugController {

    private final BugService bugService;

    @Autowired
    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    // getAll

    // get

    // add
    @GetMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody BugAddBindingModel bugAddBindingModel,
                                 BindingResult bindingResult,
                                 UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(bugAddBindingModel);
        }

        BugServiceModel bug = this.bugService.add(bugAddBindingModel);

        return ResponseEntity.created(uriComponentsBuilder.path("/bugs/{bugId}")
                .buildAndExpand(bug.getId())
                .toUri()).build();
    }

    // update

    // delete
}
