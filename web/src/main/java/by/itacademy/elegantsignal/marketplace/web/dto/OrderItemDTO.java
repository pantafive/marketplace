package by.itacademy.elegantsignal.marketplace.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
public class OrderItemDTO {

	private Integer id;
	private String productTitle;
	private BigDecimal amount;
	private List<String> tokenList;

}
