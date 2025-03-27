package com.springcourse.simpleCrud.route.user;

import com.springcourse.simpleCrud.model.Response.BaseResponse;
import com.springcourse.simpleCrud.model.schema.UserProfile;
import com.springcourse.simpleCrud.route.user.jwt.JWTAuthService;
import com.springcourse.simpleCrud.route.user.model.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JWTAuthService jwtAuthService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public UserService(AuthenticationManager authenticationManager, JWTAuthService jwtAuthService,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtAuthService = jwtAuthService;
    }

    public ResponseEntity<BaseResponse<UserProfile>> register(UserProfile user) {
        var userExists = userRepository.findByEmail(user.getEmail()).orElse(null);

        if (userExists != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>("User already exists", null));
        }

        // encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("User added successfully", user));
    }

    public ResponseEntity<BaseResponse<String>> login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse<>("User not found", null));
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if (authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BaseResponse<>("Login success", jwtAuthService.generateToken(request.getEmail())));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>("Failed to login!", null));
        }
    }
}
