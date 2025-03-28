package com.springcourse.simpleCrud.route.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JWTFilter extends OncePerRequestFilter {

    JWTAuthService jwtAuthService;

    public JWTFilter(JWTAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtAuthService.validateToken(token)) {
                String username = jwtAuthService.extractUsername(token);
                String role = jwtAuthService.extractRole(token);
                String pass = jwtAuthService.extractPassword(token);

                List<GrantedAuthority> authorities = new ArrayList<>();

                if (role.equals("ROLE_ADMIN")) {
                    authorities.addAll(
                            List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN")));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }

                User user = new User(username, pass, authorities); // Roles can be added here

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword(), user.getAuthorities()));
            }
        }

        filterChain.doFilter(request, response);
    }
}
