package com.springcourse.simpleCrud.route.home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String greet() {
        return "Helo, this is Arief!!";
    }
}
