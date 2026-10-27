package com.fonseca.algashop.authorizationserver.presentation;

import com.fonseca.algashop.authorizationserver.application.user.management.AuthUserInput;
import com.fonseca.algashop.authorizationserver.application.user.management.AuthUserManagementApplicationService;
import com.fonseca.algashop.authorizationserver.application.user.management.AuthUserUpdateInput;
import com.fonseca.algashop.authorizationserver.application.user.query.AuthUserFilter;
import com.fonseca.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.fonseca.algashop.authorizationserver.application.user.query.AuthUserQueryService;
import com.fonseca.algashop.authorizationserver.application.user.query.PageModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.fonseca.algashop.authorizationserver.infrastructure.security.check.SecurityAnnotations.CanReadUsers;
import static com.fonseca.algashop.authorizationserver.infrastructure.security.check.SecurityAnnotations.CanWriteUsers;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final AuthUserManagementApplicationService managementService;
	private final AuthUserQueryService queryService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CanWriteUsers
	public AuthUserOutput create(@RequestBody @Valid AuthUserInput input) {
		return managementService.create(input);
	}

	@CanReadUsers
	@GetMapping
	public PageModel<AuthUserOutput> findAll(AuthUserFilter filter) {
		return queryService.findAll(filter);
	}

	@CanReadUsers
	@GetMapping("/{userId}")
	public AuthUserOutput findById(@PathVariable UUID userId) {
		return queryService.findById(userId);
	}

	@CanWriteUsers
	@PutMapping("/{userId}")
	public AuthUserOutput update(@PathVariable UUID userId, @RequestBody @Valid AuthUserUpdateInput input) {
		return managementService.update(userId, input);
	}

	@CanWriteUsers
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID userId) {
		managementService.delete(userId);
	}
}