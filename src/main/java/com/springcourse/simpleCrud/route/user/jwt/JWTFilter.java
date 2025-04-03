package com.springcourse.simpleCrud.route.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.http.MediaType;
import java.util.*;

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

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, "Missing or invalid Authorization header!", HttpStatus.UNAUTHORIZED);
            return;
        }

        String token = authorizationHeader.substring(7);

        if (jwtAuthService.validateToken(token)) {
            String username = jwtAuthService.extractUsername(token);
            String role = jwtAuthService.extractRole(token);
            String pass = jwtAuthService.extractPassword(token);

            List<GrantedAuthority> authorities = new ArrayList<>();

            if (role.equals("ROLE_ADMIN")) {
                authorities.addAll(
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            User user = new User(username, pass, authorities); // Roles can be added here

            /*
             * the authorities is used for MethodSecurity by checking the role that we pass,
             * the example of MethodSecurity is in ProductController class
             */
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword(), user.getAuthorities()));
        }

        filterChain.doFilter(request, response);
    }

    /*
     * This method will send error response to the client
     */
    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("trace", status.getReasonPhrase());
        body.put("data", null);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
