package com.example.demo.controller;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home() {
        log.info("GET /");
        return "index";
    }

    @GetMapping("/user")
    public void user() {
        log.info("GET /user");
    }

    @GetMapping("/member")
    public void member() {
        log.info("GET /member");
    }

    @GetMapping("/admin")
    public void admin() {
        log.info("GET /admin");
    }
}
