package com.planteshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/favicon.svg", "/favicon.ico", "/plants", "/plants/**", "/css/**", "/js/**", "/webjars/**", "/login", "/register", "/api/plants/**", "/cart/**" ).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/orders/**", "/my_profile/**").authenticated()
                .anyRequest().permitAll() // Modifié pour permettre l'accès à tout le reste
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/plants") // Page après login réussi
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") // Page après logout
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
