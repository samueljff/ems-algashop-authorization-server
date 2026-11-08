package com.fonseca.algashop.authorizationserver.infrastructure.security.token;

import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserType;
import com.fonseca.algashop.authorizationserver.infrastructure.security.query.AuthUserClientScopesQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScopePolicyService {

    private final AuthUserClientScopesQueryService scopesQueryService;

    public Set<String> resolveScopes(AuthUserType role, String clientId, Set<String> authorizedScopes) {

        if (authorizedScopes.isEmpty()) {
            return new HashSet<>();
        }

        Set<String> allowedScopes = scopesQueryService.findAllowedScopesByRolesAndClientId(role, clientId);

        if (allowedScopes.isEmpty()) {
            return new HashSet<>(authorizedScopes);
        }

        return allowedScopes.stream()
            .filter(allowedScopes::contains)
            .collect(Collectors.toSet());
    }
}
