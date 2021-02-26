package com.syn.issuetracker.web;

import com.syn.issuetracker.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//    @GetMapping("/add")
//    public ResponseEntity<?> add() {
//
//        return ResponseEntity.ok().build();
//    }

    // update

    // delete
}
