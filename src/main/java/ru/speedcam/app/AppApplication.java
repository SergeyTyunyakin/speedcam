package ru.speedcam.app;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataSize;

import static org.springframework.util.unit.DataUnit.MEGABYTES;

@SpringBootApplication
@ComponentScan(basePackages = "ru.speedcam")
@EnableJpaRepositories(basePackages = "ru.speedcam.repositories")
@EntityScan(basePackages = "ru.speedcam.models")
public class AppApplication {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        long maxFileSize = 100l;
        long maxRequestSize = 200l;
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(maxFileSize ,MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(maxRequestSize ,MEGABYTES));
        return factory.createMultipartConfig();
    }

}
