package com.joserodriguezdeveloper.BlogService.controlador;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/secure")
    public String secure(Authentication auth) {
        return "secure ok, user=" + auth.getName();
    }
}
