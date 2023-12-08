package com.example.demo.config;


import com.example.demo.config.auth.exceptionHandler.CustomAccessDeniedHandler;
import com.example.demo.config.auth.exceptionHandler.CustomAuthenticationEntryPoint;
import com.example.demo.config.auth.loginHandler.CustomAuthenticationFailureHandler;
import com.example.demo.config.auth.loginHandler.CustomLoginSuccessHandler;
import com.example.demo.config.auth.logoutHandler.CustomLogoutHandler;
import com.example.demo.config.auth.logoutHandler.CustomLogoutSuccessHandler;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {


    @Autowired
    private HikariDataSource dataSource;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {

        //CSRF 비활성화
        http.csrf(
                (config)->{ config.disable(); }
        );

        //요청 URL별 접근 제한
        http.authorizeHttpRequests(
                authorize->{
                    authorize.requestMatchers("/","/login").permitAll();
                    authorize.requestMatchers("/user").hasRole("USER"); // ROLE_USER
                    authorize.requestMatchers("/member").hasRole("MEMBER"); // ROLE_MEMBER
                    authorize.requestMatchers("/admin").hasRole("ADMIN"); // ROLE_ADMIN
                    authorize.anyRequest().authenticated();
                }
        );
        //로그인
        http.formLogin(login->{
            login.permitAll();
            login.loginPage("/login");
            login.successHandler(new CustomLoginSuccessHandler());
            login.failureHandler(new CustomAuthenticationFailureHandler());

        });

        //로그아웃
        http.logout(
                (logout)->{
                    logout.permitAll();
                    logout.logoutUrl("/logout");
                    logout.addLogoutHandler(new CustomLogoutHandler());
                    logout.logoutSuccessHandler(new CustomLogoutSuccessHandler());
                }
        );

        //예외처리
        http.exceptionHandling(
                ex->{
                    ex.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new CustomAccessDeniedHandler());
                }
        );

        //RememberMe
        http.rememberMe(
                rm->{
                    rm.key("rememberMeKey");
                    rm.rememberMeParameter("remember-me");
                    rm.alwaysRemember(false);
                    rm.tokenValiditySeconds(3600);
                    rm.tokenRepository(tokenRepository());
                }
        );

        return http.build();
    }

    // REMEMBER ME 처리 BEAN
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
//        repo.setCreateTableOnStartup(true);
        repo.setDataSource(dataSource);
        return repo;
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
