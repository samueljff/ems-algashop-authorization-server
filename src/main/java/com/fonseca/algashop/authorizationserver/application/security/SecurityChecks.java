package com.fonseca.algashop.authorizationserver.application.security;

import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserType;

import java.util.UUID;

public interface SecurityChecks {
	UUID getAuthenticatedUserId();
	boolean isAuthenticated();
	boolean isMachineAuthenticated();
	boolean canAccessOwnProfile();
	boolean canRegisterUserOfType(AuthUserType registrationType);
	boolean canEditUser(AuthUserType userType, UUID editUserId);
	boolean canChangeUserType(AuthUserType currentType, AuthUserType newType);
}