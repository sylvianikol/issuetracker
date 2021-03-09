package com.syn.issuetracker.config;

import com.syn.issuetracker.utils.validation.ValidationUtil;
import com.syn.issuetracker.utils.validation.ValidationUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

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
}
