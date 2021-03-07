package com.syn.issuetracker.web;

import com.syn.issuetracker.web.AuthController;
import com.syn.issuetracker.web.TasksController;
import com.syn.issuetracker.web.UserController;
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
    public void contextLoads() throws Exception {
        assertThat(authController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(tasksController).isNotNull();
    }
}
