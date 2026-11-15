package com.fonseca.algashop.authorizationserver.application.user.management;

import com.fonseca.algashop.authorizationserver.application.security.SecurityChecks;
import com.fonseca.algashop.authorizationserver.application.user.UserAccountProperties;
import com.fonseca.algashop.authorizationserver.application.user.mail.AuthUserMailSender;
import com.fonseca.algashop.authorizationserver.application.user.query.AuthUserNotFoundException;
import com.fonseca.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUser;
import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserPasswordManager;
import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserRepository;
import com.fonseca.algashop.authorizationserver.domain.model.user.VerificationTokenHasher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUserManagementApplicationService {

	private final AuthUserRepository authUserRepository;
	private final SecurityChecks securityCheck;
	private final UserAccountProperties userAccountProperties;
	private final AuthUserPasswordManager passwordManager;
	private final VerificationTokenHasher tokenHasher;
	private final AuthUserMailSender authUserMailSender;

	public AuthUserOutput create(AuthUserInput input) {

		if (!securityCheck.canRegisterUserOfType(input.getType())) {
			throw new AccessDeniedException("Cannot register user of type " + input.getType());
		}

		if (authUserRepository.existsByEmail(input.getEmail())) {
			throw new AuthUserEmailAlreadyInUseException(input.getEmail());
		}

		AuthUser user = AuthUser.brandNew(
				input.getEmail(),
				input.getName(),
				input.getType(),
				passwordManager
		);

		String plainToken = user.generateVerificationToken(userAccountProperties.getToken().getActivationTtl(),
			tokenHasher);

		authUserMailSender.sendActivationEmail(user, plainToken);

		return AuthUserOutput.from(authUserRepository.save(user));
	}

	public AuthUserOutput update(UUID userId, AuthUserUpdateInput input) {
		AuthUser authUser = authUserRepository.findById(userId)
			.orElseThrow(() -> new AuthUserNotFoundException(userId));

		verifyCanEditUser(authUser, input);

		authUser.setName(input.getName());
		authUser.setType(input.getType());
		authUser.setEnabled(input.isEnabled());

		return AuthUserOutput.from(authUserRepository.save(authUser));
	}

	private void verifyCanEditUser(AuthUser authUser, AuthUserUpdateInput input) {

		if (!securityCheck.canEditUser(authUser.getType(), authUser.getId())) {
			throw new AccessDeniedException("Cannot edit user of type " + authUser.getType());
		}

		if (!securityCheck.canChangeUserType(authUser.getType(), input.getType())) {
			throw new AccessDeniedException("Cannot change user type to " + input.getType());
		}
	}

	public void delete(UUID userId) {
		AuthUser user = authUserRepository.findById(userId)
			.orElseThrow(() -> new AuthUserNotFoundException(userId));
		user.anonymize();

		authUserRepository.save(user);
	}
}