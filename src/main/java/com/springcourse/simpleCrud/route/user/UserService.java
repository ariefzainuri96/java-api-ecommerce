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

    public ResponseEntity<BaseResponse<UserProfile>> getProfile(int id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse<>("User profile retrieved successfully", userRepository.findById(id).orElse(null)));
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
        /*
         * When we create this Authentication object, it will refer to
         * SecurityUserService class and call loadByUsername method
         * then we can get the data using getPrincipal() method
         */
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        /*
         * getPrincipal() method will return UserPrincipal object that implements
         * UserDetails
         */
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String role = userPrincipal.getAuthorities().stream().findFirst().get().getAuthority();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BaseResponse<>("Login success",
                            jwtAuthService.generateToken(userPrincipal.getUsername(), userPrincipal.getPassword(),
                                    role)));

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>("Failed to login!", null));
        }
    }
}
