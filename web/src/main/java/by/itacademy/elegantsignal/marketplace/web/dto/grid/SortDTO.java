package by.itacademy.elegantsignal.marketplace.web.dto.grid;

public class SortDTO {

	private String column;

	private boolean ascending;

	public SortDTO(final String column) {
		this(column, false);
	}

	public SortDTO(final String column, final boolean ascending) {
        this.column = column;
		this.ascending = ascending;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(final String column) {
		this.column = column;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(final boolean ascending) {
		this.ascending = ascending;
	}

}
