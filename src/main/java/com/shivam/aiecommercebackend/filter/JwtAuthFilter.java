package com.shivam.aiecommercebackend.filter;

import com.shivam.aiecommercebackend.entity.UserDetailsPrincipal;
import com.shivam.aiecommercebackend.service.CustomUserDetailsService;
import com.shivam.aiecommercebackend.utility.JwtAuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtAuthUtil jwtAuthUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getParameter("Authorization"));
        String authHeader=request.getHeader("Authorization");
        String token=null;
        String username=null;
//        Checking Header
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            username=jwtAuthUtil.getUsername(token);
//            Checking authentication
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetailsPrincipal userDetailsPrincipal= (UserDetailsPrincipal) customUserDetailsService.loadUserByUsername(username);
//                Validating token
                if(jwtAuthUtil.validateToken(token,userDetailsPrincipal)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetailsPrincipal,null,userDetailsPrincipal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
//        Calling next filter
        filterChain.doFilter(request,response);
    }
}
