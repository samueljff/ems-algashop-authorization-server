package com.fonseca.algashop.authorizationserver.presentation;

import com.fonseca.algashop.authorizationserver.infrastructure.security.AlgashopSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AlgashopSecurityProperties algashopSecurityProperties;

    @GetMapping("/")
    public String home(){
        return "redirect:" + algashopSecurityProperties.getDefaultRedirectUri();
    }
}
