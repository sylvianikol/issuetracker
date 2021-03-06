package com.syn.issuetracker;

import com.syn.issuetracker.model.enums.UserRole;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.entity.UserRoleEntity;
import com.syn.issuetracker.repository.UserRepository;
import com.syn.issuetracker.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IssueTrackerApplicationInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public IssueTrackerApplicationInit(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args)  {

        if (this.userRoleRepository.count() == 0) {
            UserRoleEntity userRole = new UserRoleEntity(UserRole.ROLE_USER);
            userRole = this.userRoleRepository.save(userRole);

            UserRoleEntity adminRole = new UserRoleEntity(UserRole.ROLE_ADMIN);
            adminRole = this.userRoleRepository.save(adminRole);

            UserRoleEntity testRole = new UserRoleEntity(UserRole.ROLE_TEST);
            testRole = this.userRoleRepository.save(testRole);

            if (this.userRepository.count() == 0) {

                UserEntity user = new UserEntity();
                user.setUsername("user");
                user.setEmail("user@mail.com");
                user.setPassword(this.passwordEncoder.encode("123"));
                user.setAuthorities(List.of(userRole, testRole));

                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setEmail("admin@mail.com");
                admin.setPassword(this.passwordEncoder.encode("123"));
                admin.setAuthorities(List.of(adminRole, userRole));

                this.userRepository.save(user);
                this.userRepository.save(admin);
            }
        }

    }
}
