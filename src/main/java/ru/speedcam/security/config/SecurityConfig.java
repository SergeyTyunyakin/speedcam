package ru.speedcam.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.speedcam.security.details.UserDetailsServiceImpl;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers(
                        "/signup/**",
                        "/upload/**",
                        "/css/**",
                        "/progress-ws/**",
                        "/vendors/**",
                        "/view/**",
                        "/ws-broadcast/**",
                        "/upload-csv-file/**").permitAll();

        http
                .authorizeHttpRequests()
                .requestMatchers("/users/**").hasAuthority("ADMIN")
//                .requestMatchers("/").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("login")
                .defaultSuccessUrl("/")
                .loginPage("/login")
                .permitAll()
                .and()
                .rememberMe().tokenValiditySeconds(86400).and()
                .rememberMe().alwaysRemember(true)
                .rememberMeParameter("remember-me")
                .tokenRepository(tokenRepository())
                .userDetailsService(userDetailsService)
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
        http.csrf().disable();

        return http.build();
    }


    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    protected void configure(AuthenticationManagerBuilder auth, WebSecurity web) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}