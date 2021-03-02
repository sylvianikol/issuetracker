package com.syn.issuetracker;

import com.syn.issuetracker.enums.UserRole;
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
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setRole(UserRole.USER);
            userRole = this.userRoleRepository.save(userRole);

            UserRoleEntity adminRole = new UserRoleEntity();
            adminRole.setRole(UserRole.ADMIN);
            adminRole = this.userRoleRepository.save(adminRole);

            if (this.userRepository.count() == 0) {

                UserEntity userEntity = new UserEntity();
                userEntity.setUsername("user");
                userEntity.setEmail("user@mail.com");
                userEntity.setPassword(this.passwordEncoder.encode("123"));
                userEntity.setAuthorities(List.of(userRole));

                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setEmail("admin@mail.com");
                admin.setPassword(this.passwordEncoder.encode("123"));
                admin.setAuthorities(List.of(adminRole, userRole));

                this.userRepository.save(userEntity);
                this.userRepository.save(admin);
            }
        }

    }
}
