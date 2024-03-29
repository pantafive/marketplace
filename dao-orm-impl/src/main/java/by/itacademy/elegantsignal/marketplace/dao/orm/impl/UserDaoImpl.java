package by.itacademy.elegantsignal.marketplace.dao.orm.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.stereotype.Repository;

import by.itacademy.elegantsignal.marketplace.dao.orm.impl.entity.BaseEntity_;
import by.itacademy.elegantsignal.marketplace.dao.orm.impl.entity.User;
import by.itacademy.elegantsignal.marketplace.dao.orm.impl.entity.User_;
import by.itacademy.elegantsignal.marketplace.daoapi.IUserDao;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IUser;
import by.itacademy.elegantsignal.marketplace.daoapi.filter.UserFilter;


@Repository
public class UserDaoImpl extends AbstractDaoImpl<IUser, Integer> implements IUserDao {

	protected UserDaoImpl() {
		super(User.class);
	}

	@Override
	public IUser createEntity() {
		return new User();
	}

	@Override
	public long getCount(final UserFilter filter) {
		final EntityManager em = getEntityManager();
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		final Root<User> from = cq.from(User.class);
		cq.select(cb.count(from));
		final TypedQuery<Long> q = em.createQuery(cq);
		return q.getSingleResult();
	}

	@Override
	public IUser findOne(final UserFilter filter) {
		final EntityManager em = getEntityManager();
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<IUser> cq = cb.createQuery(IUser.class);
		final Root<User> from = cq.from(User.class);
		cq.select(from);

		applyFilter(filter, cb, cq, from);

		final TypedQuery<IUser> q = em.createQuery(cq);
		return q.getSingleResult();
	}

	@Override
	public List<IUser> find(final UserFilter filter) {
		final EntityManager em = getEntityManager();
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<IUser> cq = cb.createQuery(IUser.class);
		final Root<User> from = cq.from(User.class);
		cq.select(from);

		if (filter.getSortColumn() != null) {
			final SingularAttribute<? super User, ?> sortProperty = toMetamodelFormat(filter.getSortColumn());
			final Path<?> expression = from.get(sortProperty);
			cq.orderBy(new OrderImpl(expression, filter.getSortOrder()));
		}

		final TypedQuery<IUser> q = em.createQuery(cq);
		setPaging(filter, q);
		return q.getResultList();
	}

	private void applyFilter(final UserFilter filter, final CriteriaBuilder cb, final CriteriaQuery<?> cq,
			final Root<User> from) {
		final List<Predicate> ands = new ArrayList<>();

		final String email = filter.getEmail();
		if (filter.getEmail() != null) {
			ands.add(cb.equal(from.get(User_.email), email));
		}

		if (!ands.isEmpty()) {
			cq.where(cb.and(ands.toArray(new Predicate[0])));
		}
	}

	private SingularAttribute<? super User, ?> toMetamodelFormat(final String sortColumn) {
		switch (sortColumn) {
			case "id":
				return BaseEntity_.id;
			case "name":
				return User_.name;
			default:
				throw new UnsupportedOperationException("sorting is not supported by column:" + sortColumn);
		}
	}

	@Override
	public IUser getFullInfo(final Integer id) {
		final EntityManager em = getEntityManager();
		final CriteriaBuilder cb = em.getCriteriaBuilder();

		final CriteriaQuery<IUser> cq = cb.createQuery(IUser.class); // define returning result
		final Root<User> from = cq.from(User.class); // define table for select

		cq.select(from); // define what need to be selected

		from.fetch(User_.role, JoinType.LEFT);
		// .. where id=...
		cq.where(cb.equal(from.get(User_.id), id)); // where id=?

		final TypedQuery<IUser> q = em.createQuery(cq);

		return q.getSingleResult();
	}
	
	@Override
	public IUser getFullInfo(final UserFilter filter) {
		final EntityManager em = getEntityManager();
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<IUser> cq = cb.createQuery(IUser.class);
		final Root<User> from = cq.from(User.class);

		cq.select(from);

		applyFilter(filter, cb, cq, from);

		from.fetch(User_.role, JoinType.LEFT);

		final TypedQuery<IUser> q = em.createQuery(cq);

		return q.getSingleResult();
	}

}
