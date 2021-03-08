package com.syn.issuetracker.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ControllersTest {

    @Autowired
    private AuthController authController;
    @Autowired
    private UserController userController;
    @Autowired
    private TasksController tasksController;

    @Test
    public void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(tasksController).isNotNull();
    }
}
