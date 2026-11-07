package com.fonseca.algashop.authorizationserver.infrastructure.persistence;

import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserType;
import com.fonseca.algashop.authorizationserver.infrastructure.security.query.AuthUserClientScopesQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JdbcAuthUserClientScopesQueryService implements AuthUserClientScopesQueryService {

    private final JdbcOperations jdbcOperations;

    private static final String SQL = """
        SELECT scope
        FROM auth_user_type_client_scope
        WHERE auth_user_type = ?
        AND client_id = ?
        """;

    @Override
    public Set<String> findAllowedScopesByRolesAndClientId(AuthUserType role, String clientId) {
        return new HashSet<>(jdbcOperations.queryForList(SQL, String.class, role.name(), clientId));
    }
}
