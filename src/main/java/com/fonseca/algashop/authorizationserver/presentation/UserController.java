package com.fonseca.algashop.authorizationserver.presentation;

import com.fonseca.algashop.authorizationserver.application.user.management.AuthUserInput;
import com.fonseca.algashop.authorizationserver.application.user.management.AuthUserManagementApplicationService;
import com.fonseca.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.fonseca.algashop.authorizationserver.infrastructure.security.check.SecurityAnnotations;
import com.fonseca.algashop.authorizationserver.infrastructure.security.check.SecurityAnnotations.CanWriteUsers;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final AuthUserManagementApplicationService managementService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CanWriteUsers
	public AuthUserOutput create(@RequestBody @Valid AuthUserInput input) {
		return managementService.create(input);
	}

}