package com.example.boat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
//@CrossOrigin(origins = "*")
@EnableWebSecurity
public class SecurityConfig {

    //authentication
    @Bean
    public UserDetailsService users() {
        // The builder will ensure the passwords are encoded before saving in memory

        User.UserBuilder users = User.withDefaultPasswordEncoder();
        UserDetails user = users
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = users
                .username("admin")
                .password("password")
                .roles("USER", "ADMIN")
                .build();
        //set user can connect with InMemoryUser
        return new InMemoryUserDetailsManager(user, admin);
    }

    //authorization
    @Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {

        //.formLogin((Customizer.withDefaults()))
        http.httpBasic();
        http
                .cors().and()
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().fullyAuthenticated()
                )
                // Disable CSRF protection for H2 Console
                .csrf().disable()
                // Authorize display H2 console in a iframe
                .headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //allow requests all origin
        configuration.addAllowedOrigin("*");
        //allow all methods requests
        configuration.addAllowedMethod("*");
        //allow all headers http
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
