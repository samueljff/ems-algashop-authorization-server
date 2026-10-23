package com.fonseca.algashop.authorizationserver.presentation;

import com.fonseca.algashop.authorizationserver.application.user.management.AuthUserInput;
import com.fonseca.algashop.authorizationserver.application.user.management.AuthUserManagementApplicationService;
import com.fonseca.algashop.authorizationserver.application.user.query.AuthUserOutput;
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
	public AuthUserOutput create(@RequestBody @Valid AuthUserInput input) {
		return managementService.create(input);
	}

}