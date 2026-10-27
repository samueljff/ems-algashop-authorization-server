package com.fonseca.algashop.authorizationserver.application.user.query;

import java.util.UUID;

public interface AuthUserQueryService {
	AuthUserOutput findById(UUID userId);
	PageModel<AuthUserOutput> findAll(AuthUserFilter filter);
}