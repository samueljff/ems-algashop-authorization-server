package com.fonseca.algashop.authorizationserver.application.user.query;

import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserType;

import java.util.UUID;

public class AuthUserOutputTestDataBuilder {
    private AuthUserOutputTestDataBuilder() {}

    public static AuthUserOutput.AuthUserOutputBuilder aUser() {
        return AuthUserOutput.builder()
            .id(UUID.randomUUID())
            .name("John Doe")
            .email("john.doe@email.com")
            .type(AuthUserType.MANAGER)
            .enabled(true);
    }

    public static AuthUserOutput.AuthUserOutputBuilder aUserAlt() {
        return AuthUserOutput.builder()
            .id(UUID.randomUUID())
            .name("Peter Parker")
            .email("peter.parker@email.com")
            .type(AuthUserType.OPERATOR)
            .enabled(true);
    }
}