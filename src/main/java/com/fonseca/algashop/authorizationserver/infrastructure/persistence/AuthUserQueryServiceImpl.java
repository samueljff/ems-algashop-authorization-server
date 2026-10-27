package com.fonseca.algashop.authorizationserver.infrastructure.persistence;

import com.fonseca.algashop.authorizationserver.application.user.query.*;
import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUser;
import com.fonseca.algashop.authorizationserver.domain.model.user.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthUserQueryServiceImpl implements AuthUserQueryService {

	private final AuthUserRepository authUserRepository;
	private final EntityManager entityManager;

	@Override
	public AuthUserOutput findById(UUID userId) {
		return authUserRepository.findById(userId)
				.map(AuthUserOutput::from)
				.orElseThrow(() -> new AuthUserNotFoundException(userId));
	}

	@Override
	public PageModel<AuthUserOutput> findAll(AuthUserFilter filter) {
		Long total = countTotalResults(filter);

		PageRequest pageable = PageRequest.of(filter.getPage(), filter.getSize());

		if (total.equals(0L)) {
			return PageModel.of(new PageImpl<>(new ArrayList<>(), pageable, 0L));
		}

		return findUsersByFilter(filter, total);
	}

	private Long countTotalResults(AuthUserFilter filter) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		Root<AuthUser> root = query.from(AuthUser.class);

		query.select(criteriaBuilder.count(root))
				.where(toPredicates(criteriaBuilder, root, filter));

		return entityManager.createQuery(query).getSingleResult();
	}

	private PageModel<AuthUserOutput> findUsersByFilter(AuthUserFilter filter, Long total) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AuthUserOutput> query = criteriaBuilder.createQuery(AuthUserOutput.class);
		Root<AuthUser> root = query.from(AuthUser.class);

		query.select(criteriaBuilder.construct(
						AuthUserOutput.class,
						root.get("id"),
						root.get("name"),
						root.get("email"),
						root.get("type"),
						root.get("enabled")
				))
				.where(toPredicates(criteriaBuilder, root, filter))
				.orderBy(toSortOrder(criteriaBuilder, root, filter));

		var typedQuery = entityManager.createQuery(query);
		typedQuery.setFirstResult(filter.getSize() * filter.getPage());
		typedQuery.setMaxResults(filter.getSize());

		return PageModel.of(new PageImpl<>(typedQuery.getResultList(),
				PageRequest.of(filter.getPage(), filter.getSize()), total));
	}

	private Predicate[] toPredicates(CriteriaBuilder builder, Root<AuthUser> root, AuthUserFilter filter) {
		List<Predicate> predicates = new ArrayList<>();

		if (filter.getEmail() != null) {
			predicates.add(builder.like(builder.lower(root.get("email")),
					"%" + filter.getEmail().toLowerCase() + "%"));
		}

		if (filter.getType() != null) {
			predicates.add(builder.equal(root.get("type"), filter.getType()));
		}

		return predicates.toArray(new Predicate[0]);
	}

	private Order toSortOrder(CriteriaBuilder builder, Root<AuthUser> root, AuthUserFilter filter) {
		return filter.getDirection().isAscending()
				? builder.asc(root.get(filter.getSort()))
				: builder.desc(root.get(filter.getSort()));
	}

}