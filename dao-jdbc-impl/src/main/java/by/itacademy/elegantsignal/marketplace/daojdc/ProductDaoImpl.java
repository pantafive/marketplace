package by.itacademy.elegantsignal.marketplace.daojdc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import by.itacademy.elegantsignal.marketplace.daoapi.IProductDao;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.ProductType;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IProduct;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IUserAccount;
import by.itacademy.elegantsignal.marketplace.daoapi.filter.ProductFilter;
import by.itacademy.elegantsignal.marketplace.daojdc.entity.Product;
import by.itacademy.elegantsignal.marketplace.daojdc.entity.UserAccount;
import by.itacademy.elegantsignal.marketplace.daojdc.util.PreparedStatementAction;

public class ProductDaoImpl extends AbstractDaoImpl<IProduct, Integer> implements IProductDao {

	@Override
	public IProduct createEntity() {
		return new Product();
	}

	@Override
	public void insert(final IProduct entity) {
		executeStatement(new PreparedStatementAction<IProduct>(
				String.format("insert into %s (user_account_id, type, price, created, updated) values(?,?,?,?,?)", getTableName()), true) {
			@Override
			public IProduct doWithPreparedStatement(final PreparedStatement pStmt) throws SQLException {
				pStmt.setInt(1, entity.getUserAccount().getId());
				pStmt.setString(2, entity.getType().name());
				pStmt.setBigDecimal(3, entity.getPrice());
				pStmt.setObject(4, entity.getCreated(), Types.TIMESTAMP);
				pStmt.setObject(5, entity.getUpdated(), Types.TIMESTAMP);

				pStmt.executeUpdate();

				final ResultSet rs = pStmt.getGeneratedKeys();
				rs.next();
				final int id = rs.getInt("id");

				rs.close();

				entity.setId(id);
				return entity;
			}
		});

	}


	@Override
	public void update(final IProduct entity) {
		throw new RuntimeException("will be implemented in ORM layer. Too complex for plain jdbc ");
	}

	@Override
	public List<IProduct> find(final ProductFilter filter) {
		throw new RuntimeException("will be implemented in ORM layer. Too complex for plain jdbc ");
	}

	@Override
	public long getCount(final ProductFilter filter) {
		throw new RuntimeException("will be implemented in ORM layer. Too complex for plain jdbc ");
	}

	@Override
	protected IProduct parseRow(final ResultSet resultSet) throws SQLException {
		final IProduct entity = createEntity();
		entity.setId((Integer) resultSet.getInt("id"));
		
		final IUserAccount userAccount = new UserAccount();
		userAccount.setId(resultSet.getInt("user_account_id"));
		entity.setUserAccount(userAccount);
		
		entity.setType(ProductType.valueOf(resultSet.getString("type")));
		entity.setPrice(resultSet.getBigDecimal("price"));
		entity.setCreated(resultSet.getTimestamp("created"));
		entity.setUpdated(resultSet.getTimestamp("updated"));
		return entity;
	}

	@Override
	protected String getTableName() {
		return "product";
	}

	

}