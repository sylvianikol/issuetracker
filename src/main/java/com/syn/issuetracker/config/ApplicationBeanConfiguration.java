package com.syn.issuetracker.config;

import com.syn.issuetracker.utils.ValidationUtil;
import com.syn.issuetracker.utils.ValidationUtilImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Properties;

@Configuration
@ImportResource(value = "classpath:/appXMLContext.xml")
@PropertySource(value = "classpath:/application.properties")
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

//    @Bean
//    public JavaMailSender javaMailSender(@Value("${spring.mail.host}") String emailHost,
//                                         @Value("${spring.mail.username}") String username,
//                                         @Value("${spring.mail.password}") String password,
//                                         @Value("${spring.mail.port}") String port) {
//
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(emailHost);
//        mailSender.setUsername(username);
//        mailSender.setPassword(password);
//        mailSender.setPort(Integer.parseInt(port));
//
//        Properties properties = new Properties();
//        properties.setProperty("mail.from.email", System.getenv("MAIL_USER"));
//        properties.setProperty("mail.smtp.auth", "true");
//        properties.setProperty("mail.smtp.starttls.enable", "true");
//        properties.setProperty("mail.debug", "true");
//
//        mailSender.setJavaMailProperties(properties);
//        return mailSender;
//    }
}
