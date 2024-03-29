package by.itacademy.elegantsignal.marketplace.service;

import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IProduct;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IUser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ProductServiceTest extends AbstractTest {

	@Test
	public void testCreate() {
		final IProduct entity = saveNewProduct(productService.createEntity());

		// TODO: fix me full load
		//		final IProduct entityFromDb = productService.get(entity.getId());
		final IProduct entityFromDb = productService.getFullInfo(entity.getId());

		assertNotNull(entityFromDb);
		assertEquals(entity.getUser().getId(), entityFromDb.getUser().getId());
		assertNotNull(entityFromDb.getId());
		assertNotNull(entityFromDb.getCreated());
		assertNotNull(entityFromDb.getUpdated());
	}

	@Test
	public void testGetAll() {
		final int initialCount = productService.getAll().size();

		final int randomObjectsCount = getRandomObjectsCount();
		for (int i = 0; i < randomObjectsCount; i++) {
			saveNewProduct(productService.createEntity());
		}

		final List<IProduct> allEntities = productService.getAll();

		for (final IProduct entityFromDb : allEntities) {
			assertNotNull(entityFromDb.getId());
			assertNotNull(entityFromDb.getUser());
			assertNotNull(entityFromDb.getPrice());
			assertNotNull(entityFromDb.getCreated());
			assertNotNull(entityFromDb.getUpdated());
		}

		assertEquals(randomObjectsCount + initialCount, allEntities.size());
	}

	@Test
	public void testDelete() {
		final IProduct entity = saveNewProduct(productService.createEntity());
		productService.delete(entity.getId());
		assertNull(productService.get(entity.getId()));
	}

	@Test
	public void testDeleteAll() {
		saveNewProduct(productService.createEntity());
		productService.deleteAll();
		assertEquals(0, productService.getAll().size());
	}

	@Test
	public void testGetProductsByUserId() {
		final IUser user = saveNewUser(userService.createEntity());
		for (int i = 0; i < 3; i++) {
			final IProduct product = saveNewProduct(productService.createEntity().setUser(user));
			saveNewBook(bookService.createEntity().setProduct(product));
			saveNewProduct(productService.createEntity()); // Create product with dummy user
		}

		final List<IProduct> productList = productService.getProductsByUserId(user.getId());
		assertEquals(3, productList.size());
		productList.forEach(product -> assertNotNull(product.getBook()));
	}
}
