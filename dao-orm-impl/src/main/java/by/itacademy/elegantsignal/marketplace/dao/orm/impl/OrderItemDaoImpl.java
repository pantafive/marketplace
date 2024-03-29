package by.itacademy.elegantsignal.marketplace.dao.orm.impl;

import by.itacademy.elegantsignal.marketplace.dao.orm.impl.entity.OrderItem;
import by.itacademy.elegantsignal.marketplace.dao.orm.impl.entity.OrderItem_;
import by.itacademy.elegantsignal.marketplace.dao.orm.impl.entity.Order_;
import by.itacademy.elegantsignal.marketplace.dao.orm.impl.entity.Product_;
import by.itacademy.elegantsignal.marketplace.daoapi.IOrderItemDao;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IOrderItem;
import by.itacademy.elegantsignal.marketplace.daoapi.filter.OrderItemFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Repository
public class OrderItemDaoImpl extends AbstractDaoImpl<IOrderItem, Integer> implements IOrderItemDao {

	@PersistenceContext private EntityManager entityManager;

	protected OrderItemDaoImpl() {
		super(OrderItem.class);
	}

	@Override
	public IOrderItem createEntity() {
		return new OrderItem();
	}

	@Override
	public List<IOrderItem> find(final OrderItemFilter filter) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<IOrderItem> criteriaQuery = criteriaBuilder.createQuery(IOrderItem.class);
		final Root<OrderItem> from = criteriaQuery.from(OrderItem.class);
		criteriaQuery.select(from);

		from.fetch(OrderItem_.product, JoinType.LEFT).fetch(Product_.book, JoinType.LEFT);
		from.fetch(OrderItem_.product, JoinType.LEFT).fetch(Product_.user, JoinType.LEFT);
		from.fetch(OrderItem_.order, JoinType.LEFT);
		from.fetch(OrderItem_.downloadList, JoinType.LEFT);

		applyFilter(filter, criteriaBuilder, criteriaQuery, from);

		final TypedQuery<IOrderItem> query = entityManager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@Override
	public long getCount(final OrderItemFilter filter) {
		// TODO Auto-generated method stub
		System.err.println("UNIMPLEMENTED: getCount(); Timestamp: 3:32:28 PM");
		throw new UnsupportedOperationException("UNIMPLEMENTED: getCount(); Timestamp: 3:32:28 PM");
		// return 0;
	}

	@Override
	public IOrderItem findOne(OrderItemFilter filter) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<IOrderItem> criteriaQuery = criteriaBuilder.createQuery(IOrderItem.class);
		final Root<OrderItem> from = criteriaQuery.from(OrderItem.class);
		criteriaQuery.select(from);

		from.fetch(OrderItem_.product, JoinType.LEFT);

		applyFilter(filter, criteriaBuilder, criteriaQuery, from);

		final TypedQuery<IOrderItem> query = entityManager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	@Override public BigDecimal sumAmount(final OrderItemFilter filter) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<BigDecimal> criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
		final Root<OrderItem> from = criteriaQuery.from(OrderItem.class);

		applyFilter(filter, criteriaBuilder, criteriaQuery, from);

		criteriaQuery.select(criteriaBuilder.sum(from.get(OrderItem_.amount)));
		final TypedQuery<BigDecimal> query = entityManager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	private <T> void applyFilter(
		final OrderItemFilter filter,
		final CriteriaBuilder criteriaBuilder,
		final CriteriaQuery<T> criteriaQuery,
		final Root<OrderItem> from) {

		final List<Predicate> ands = new ArrayList<>();

		if (filter.getId() != null) {
			ands.add(criteriaBuilder.equal(from.get(OrderItem_.id), filter.getId()));
		}

		if (filter.getUserId() != null) {
			ands.add(criteriaBuilder.equal(from.get(OrderItem_.order).get(Order_.user), filter.getUserId()));
		}

		if (filter.getProductOwnerId() != null) {
			ands.add(criteriaBuilder.equal(from.get(OrderItem_.product).get(Product_.user), filter.getProductOwnerId()));
		}

		if (!filter.getOrderIds().isEmpty()) {
			final List<Predicate> predicates = new ArrayList<>();
			filter.getOrderIds().forEach(orderId ->
				predicates.add(criteriaBuilder.equal(from.get(OrderItem_.order), orderId))
			);
			ands.add(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
		}

		if (!filter.getExcludeOrderStatusList().isEmpty()) {
			final List<Predicate> predicates = new ArrayList<>();
			filter.getExcludeOrderStatusList().forEach(excludeStatus ->
				predicates.add(criteriaBuilder.notEqual(from.get(OrderItem_.order).get(Order_.status), excludeStatus))
			);
			ands.add(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
		}

		if (!filter.getOrderStatusList().isEmpty()) {
			final List<Predicate> predicates = new ArrayList<>();
			filter.getOrderStatusList().forEach(status ->
				predicates.add(criteriaBuilder.equal(from.get(OrderItem_.order).get(Order_.status), status))
			);
			ands.add(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
		}

		if (!ands.isEmpty()) {
			criteriaQuery.where(criteriaBuilder.and(ands.toArray(new Predicate[0])));
		}
	}
}


