package by.itacademy.elegantsignal.marketplace.dao.orm.impl.entity;

import by.itacademy.elegantsignal.marketplace.dao.orm.converter.FileAtributeConverter;
import by.itacademy.elegantsignal.marketplace.dao.orm.converter.LocalDateAttributeConverter;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IBook;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IGenre;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IProduct;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Indexed
public class Book implements IBook {

	@Id
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Product.class)
	@PrimaryKeyJoinColumn
	private IProduct product;

	@JoinTable(name = "book_2_genre", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
	@ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY)
	private Set<IGenre> genre = new HashSet<>();

	@Column
	private String title;

	@Column
	@Convert(converter = FileAtributeConverter.class)
	private File cover;

	@Column
	@Convert(converter = LocalDateAttributeConverter.class)
	private LocalDate published;

	@Column
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String description;

	@Column(updatable = false)
	private Date created;

	@Column
	private Date updated;

	@Column
	@Convert(converter = FileAtributeConverter.class)
	private File pdf;

	@Override public Integer getId() {
		return id;
	}

	@Override public void setId(final Integer id) {
		this.id = id;
	}

	@Override public Set<IGenre> getGenre() {
		return genre;
	}

	@Override public void setGenre(final Set<IGenre> genre) {
		this.genre = genre;
	}

	@Override public File getPdf() {
		return pdf;
	}

	@Override public void setPdf(final File pdf) {
		this.pdf = pdf;
	}

	@Override public IProduct getProduct() {
		return product;
	}

	@Override public void setProduct(final IProduct product) {
		this.product = product;
	}

	@Override public String getTitle() {
		return title;
	}

	@Override public void setTitle(final String title) {
		this.title = title;
	}

	@Override public File getCover() {
		return cover;
	}

	@Override public void setCover(final File cover) {
		this.cover = cover;
	}

	@Override public LocalDate getPublished() {
		return published;
	}

	@Override public void setPublished(final LocalDate published) {
		this.published = published;
	}

	@Override public String getDescription() {
		return description;
	}

	@Override public void setDescription(final String description) {
		this.description = description;
	}

	@Override public Date getCreated() {
		return created;
	}

	@Override public void setCreated(final Date created) {
		this.created = created;
	}

	@Override public Date getUpdated() {
		return updated;
	}

	@Override public void setUpdated(final Date updated) {
		this.updated = updated;
	}

}
