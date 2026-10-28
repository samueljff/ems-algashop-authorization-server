package com.fonseca.algashop.authorizationserver.application.user.query;

import com.fonseca.algashop.authorizationserver.application.AbstractApplicationIT;
import com.fonseca.algashop.authorizationserver.domain.model.IdGenerator;
import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUser;
import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserRepository;
import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserTestDataBuilder;
import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class AuthUserQueryServiceIT extends AbstractApplicationIT {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private AuthUserQueryService authUserQueryService;

    @MockitoBean
    private RegisteredClientRepository registeredClientRepository;

    @Test
    void shouldFindBydId() {
        var authUserBuilder = AuthUserTestDataBuilder.aUser();
        AuthUser authUser = authUserBuilder.name("Pedro Silva")
            .email("pedro.silva@email.com")
            .type(AuthUserType.MANAGER)
            .enabled(true)
            .build();

        authUserRepository.saveAndFlush(authUser);

        AuthUserOutput authUserOutput = authUserQueryService.findById(authUser.getId());

        assertThat(authUserOutput)
            .satisfies(userOutput -> {
                assertThat(userOutput.getId()).isEqualTo(authUser.getId());
                assertThat(userOutput.getName()).isEqualTo(authUser.getName());
                assertThat(userOutput.getEmail()).isEqualTo(authUser.getEmail());
                assertThat(userOutput.getType()).isEqualTo(authUser.getType());
                assertThat(userOutput.isEnabled()).isEqualTo(authUser.isEnabled());
            });
    }

    @Test
    void shouldThrowAuthUserNotFoundExceptionWhenUserDoesNotExist() {
        UUID userId = IdGenerator.generateTimeBasedUUID();

        assertThatExceptionOfType(AuthUserNotFoundException.class)
            .isThrownBy(() -> authUserQueryService.findById(userId))
            .withMessage("User not found with ID: %s", userId);
    }

    @Test
    void shouldFilterUsersByEmail() {
        AuthUser john = AuthUserTestDataBuilder.aUser().build();
        AuthUser peter = AuthUserTestDataBuilder.aUserAlt().build();

        authUserRepository.saveAllAndFlush(List.of(john, peter));

        AuthUserFilter authUserFilter = new AuthUserFilter();
        authUserFilter.setEmail(john.getEmail());

        PageModel<AuthUserOutput> filtered = authUserQueryService.findAll(authUserFilter);

        assertThat(filtered.getContent())
            .singleElement()
            .satisfies(user -> {
                assertThat(user.getId()).isEqualTo(john.getId());
                assertThat(user.getName()).isEqualTo(john.getName());
                assertThat(user.getEmail()).isEqualTo(john.getEmail());
                assertThat(user.getType()).isEqualTo(john.getType());
                assertThat(user.isEnabled()).isEqualTo(john.isEnabled());
            });
        assertThat(filtered.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldFilterUsersByType() {
        AuthUser manager = AuthUserTestDataBuilder.aUser().build();
        AuthUser operator = AuthUserTestDataBuilder.aUserAlt().build();

        authUserRepository.saveAllAndFlush(List.of(manager, operator));

        AuthUserFilter filter = new AuthUserFilter();
        filter.setType(manager.getType());

        PageModel<AuthUserOutput> filtered = authUserQueryService.findAll(filter);

        assertThat(filtered.getContent())
            .isNotEmpty()
            .allSatisfy(user -> assertThat(user.getType()).isEqualTo(AuthUserType.MANAGER))
            .extracting(AuthUserOutput::getId)
            .contains(manager.getId())
            .doesNotContain(operator.getId());
    }
}