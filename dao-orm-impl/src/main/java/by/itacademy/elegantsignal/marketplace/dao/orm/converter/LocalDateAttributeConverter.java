package by.itacademy.elegantsignal.marketplace.dao.orm.converter;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(final LocalDate locDate) {
		return locDate == null ? null : Date.valueOf(locDate);
	}

	@Override
	public LocalDate convertToEntityAttribute(final Date sqlDate) {
		return sqlDate == null ? null : sqlDate.toLocalDate();
	}
}