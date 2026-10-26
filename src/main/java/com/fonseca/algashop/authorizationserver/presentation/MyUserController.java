package com.fonseca.algashop.authorizationserver.presentation;

import com.fonseca.algashop.authorizationserver.application.security.SecurityCheckApplicationService;
import com.fonseca.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.fonseca.algashop.authorizationserver.application.user.query.AuthUserQueryService;
import com.fonseca.algashop.authorizationserver.infrastructure.security.check.SecurityAnnotations.CanAccessOwnProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class MyUserController {

    private final SecurityCheckApplicationService securityCheck;
    private final AuthUserQueryService queryService;

    @GetMapping
    @CanAccessOwnProfile
    public AuthUserOutput getMe() {
        return queryService.findById(securityCheck.getAuthenticatedUserId());
    }

}
