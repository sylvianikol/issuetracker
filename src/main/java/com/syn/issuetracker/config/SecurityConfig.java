package com.syn.issuetracker.config;

import com.syn.issuetracker.security.JWTAuthenticationFilter;
import com.syn.issuetracker.security.JWTAuthorizationFilter;
import com.syn.issuetracker.security.UserEntityDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.syn.issuetracker.common.SecurityConstants.*;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserEntityDetailService userEntityDetailService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserEntityDetailService userEntityDetailService, PasswordEncoder passwordEncoder) {
        this.userEntityDetailService = userEntityDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userEntityDetailService)
                .passwordEncoder(this.passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
//                .antMatchers("/users/{\\d+}").access("hasAnyAuthority('ROLE_TOKENSAVED')")
//                .antMatchers(HttpMethod.GET, TASKS_URL).authenticated()
//                .antMatchers(HttpMethod.PUT, TASKS_URL).authenticated()
//                .antMatchers(HttpMethod.POST, TASK_ADD_URL).authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userEntityDetailService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
