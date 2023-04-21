package com.myapp.myapp.config;

import com.myapp.myapp.Data.UserRepository;
import com.myapp.myapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomLoginHandler sucessHandler;
    @Autowired
    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(userRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> {
            try {
                requests
                        // URL matching for accessibility

                        .requestMatchers("/", "/login", "/signup").permitAll()
                        .requestMatchers("/img/**","/js/**","/assets/**","/landingpage/**","/fonts/**","/bower_components/**","**/.map","**/.js", "/", "**/api/*", "**/.css").permitAll()
                        .requestMatchers("/adm/*").hasAuthority("ADMIN")
                        .requestMatchers("/exp/*").hasAnyAuthority("ADMIN","USER")
                        .anyRequest().authenticated()
                        .and()
                        // form login
                        .csrf().disable().formLogin()
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .successHandler(sucessHandler)
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .and()
                        //logout
                        .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .and()
                        .exceptionHandling()
                        .accessDeniedPage("/access-denied");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/img/**","/assets/**","/fonts/**","/bower_components/**","**/.js", "/", "**/api/*", "**/.css")
//                ;
//    }
}
