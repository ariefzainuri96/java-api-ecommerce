package com.springcourse.simpleCrud.route.user;

import com.springcourse.simpleCrud.model.Response.BaseResponse;
import com.springcourse.simpleCrud.model.schema.UserProfile;
import com.springcourse.simpleCrud.route.user.model.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserProfile>> register(@RequestBody UserProfile user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<String>> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
