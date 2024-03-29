package by.itacademy.elegantsignal.marketplace.dao.orm.impl.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IOrderItem;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IReview;


@Entity
public class Review extends BaseEntity implements IReview {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = OrderItem.class)
	private IOrderItem orderItem;

	@Column
	private Integer grade;

	@Column
	private String comment;

	@Column
	private Date created;

	@Column
	private Date updated;

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Date created) {
		this.created = created;
	}

	@Override
	public Date getUpdated() {
		return updated;
	}

	@Override
	public void setUpdated(final Date updated) {
		this.updated = updated;
	}

	@Override
	public IOrderItem getOrderItem() {
		return this.orderItem;
	}

	@Override
	public void setOrderItem(final IOrderItem orderItem) {
		this.orderItem = orderItem;

	}

	@Override
	public Integer getGrade() {
		return this.grade;
	}

	@Override
	public void setGrade(final Integer grade) {
		this.grade = grade;

	}

	@Override
	public String getComment() {
		return this.comment;
	}

	@Override
	public void setComment(final String comment) {
		this.comment = comment;
	}
}
