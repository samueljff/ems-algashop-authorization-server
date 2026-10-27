package com.fonseca.algashop.authorizationserver.contract.base;

import com.fonseca.algashop.authorizationserver.application.user.management.AuthUserManagementApplicationService;
import com.fonseca.algashop.authorizationserver.application.user.query.*;
import com.fonseca.algashop.authorizationserver.presentation.UserController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = UserController.class)
public abstract class UserBase {

    private static final UUID validUserId = UUID.fromString("019d7764-3b02-7be2-9112-039fda30e965");
    private static final UUID invalidUserId = UUID.fromString("00000111-2200-0033-2200-444000000022");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private AuthUserQueryService authUserQueryService;

    @MockitoBean
    private AuthUserManagementApplicationService userManagementApplicationService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build());

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();


        mockFindUsersFilter();
        mockFindByIdValidUserId();
        mockFindByIdInvalidUserId();
        mockDeleteInvalidUserId();
    }

    private void mockFindUsersFilter() {
        when(authUserQueryService.findAll(any(AuthUserFilter.class)))
            .thenAnswer(invocation ->
                createUsersPage(invocation.getArgument(0))
            );
    }

    private PageModel<AuthUserOutput> createUsersPage(AuthUserFilter filter) {
        return PageModel.<AuthUserOutput>builder()
            .number(0)
            .size(filter.getSize())
            .totalElements(2)
            .content(List.of(
                AuthUserOutputTestDataBuilder.aUser().build(),
                AuthUserOutputTestDataBuilder.aUserAlt().build()
            ))
            .build();
    }

    private void mockFindByIdValidUserId() {
        when(authUserQueryService.findById(validUserId))
            .thenReturn(AuthUserOutputTestDataBuilder.aUser().id(validUserId).build());
    }

    private void mockFindByIdInvalidUserId() {
        when(authUserQueryService.findById(invalidUserId))
                .thenThrow(new AuthUserNotFoundException(invalidUserId));
    }

    private void mockDeleteInvalidUserId() {
        doThrow(new AuthUserNotFoundException(invalidUserId))
            .when(userManagementApplicationService).delete(invalidUserId);
    }
}