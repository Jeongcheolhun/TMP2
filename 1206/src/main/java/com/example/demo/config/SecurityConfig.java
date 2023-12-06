package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {


        // 요청 URL 별 접근 제한
        http.authorizeHttpRequests(
                authorize->{
                    authorize.requestMatchers("/", "/login").permitAll();
                    authorize.requestMatchers("/user").hasRole("USER");   // ROLE_USER
                    authorize.requestMatchers("/member").hasRole("MEMBER");   // ROLE_MEMBER
                    authorize.requestMatchers("/admin").hasRole("ADMIN");   // ROLE_ADMIN
                    authorize.anyRequest().authenticated();
                }
        );
        // 로그인
        http.formLogin(login->{
            login.permitAll();
        });
        // 로그아웃
        return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(User.withUsername("user")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .build());

        userDetailsManager.createUser(User.withUsername("member")
                .password(passwordEncoder.encode("1234"))
                .roles("MEMBER")
                .build());

        userDetailsManager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN")
                .build());

        return userDetailsManager;
    }

    // BCryptPasswordEncoder Bean 등록 - 패스워드 검증에 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}


