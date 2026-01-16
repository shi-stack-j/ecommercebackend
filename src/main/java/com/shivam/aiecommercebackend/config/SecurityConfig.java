package com.shivam.aiecommercebackend.config;

import com.shivam.aiecommercebackend.filter.JwtAuthFilter;
import com.shivam.aiecommercebackend.service.CustomUserDetailsService;
import com.shivam.aiecommercebackend.utility.JwtAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security)throws Exception{
        return security
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(http->http
                        .requestMatchers("/admin/**","/return/policy").hasRole("ADMIN")
                        .requestMatchers("/inventory/**","/product","/category").hasAnyRole("ADMIN","MANAGER")
                        .requestMatchers("/auth/user/**","/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/auth/user/login").denyAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
//    Here i have created the custom users which are saved in InMemoryUserDetails
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1= User
//                .withDefaultPasswordEncoder()
//                .username("shivam")
//                .password("s")
//                .roles("MANAGER")
//                .build();
//
//        UserDetails user2=User
//                .withDefaultPasswordEncoder()
//                .username("gangwat")
//                .password("1234567")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user2,user1);
//    }

//    Here we are creating the bean of the authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    Manually creating the userDetails service
    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder
    ){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(customUserDetailsService);
        return  provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
