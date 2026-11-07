package com.fonseca.algashop.authorizationserver.infrastructure.security.query;

import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserType;

import java.util.Set;

public interface AuthUserClientScopesQueryService {

    Set<String> findAllowedScopesByRolesAndClientId(AuthUserType role, String clientId);
}
