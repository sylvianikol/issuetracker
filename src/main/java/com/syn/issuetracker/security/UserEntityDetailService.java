package com.syn.issuetracker.security;

import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static com.syn.issuetracker.common.ExceptionErrorMessages.EMAIL_NOT_FOUND;

@Component
public class UserEntityDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserEntityDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = this.userRepository.findByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(String.format(EMAIL_NOT_FOUND, username));
        });

        return UserDetailsImpl.build(user);
    }
}
