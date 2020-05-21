package by.itacademy.elegantsignal.marketplace.service;

import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.TransactionStatus;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.TransactionType;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.ITransaction;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IUser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TransactionServiceTest extends AbstractTest {

	@Test public void testSaveAndGet() {
		final ITransaction transaction = transactionService.createEntity()
			.setUser(saveNewUser(userService.createEntity()))
			.setType(TransactionType.PAYMENT)
			.setStatus(TransactionStatus.SUCCESS);

		transactionService.save(transaction);

		final ITransaction transactionFromDB = transactionService.getById(transaction.getId());

		assertNotNull(transactionFromDB);
		assertNotNull(transaction.getType());
		assertNotNull(transaction.getStatus());
	}

	@Test public void testGetTransactionByUserId() {
		final IUser user = saveNewUser(userService.createEntity());
		for (int i = 0; i < 3; i++) {
			final ITransaction transaction = transactionService
				.createEntity()
				.setUser(user)
				.setType(TransactionType.WITHDRAWAL)
				.setStatus(TransactionStatus.SUCCESS);
			transactionService.save(transaction);
		}

		List<ITransaction> transactionList = transactionService
			.getTransactionByUserId(user.getId(), TransactionType.WITHDRAWAL, TransactionStatus.SUCCESS);
		assertEquals(3, transactionList.size());

		transactionList = transactionService
			.getTransactionByUserId(user.getId(), TransactionType.WITHDRAWAL, null);
		assertEquals(3, transactionList.size());

		transactionList = transactionService
			.getTransactionByUserId(user.getId(), TransactionType.PAYMENT, TransactionStatus.SUCCESS);
		assertTrue(transactionList.isEmpty());

		transactionList = transactionService
			.getTransactionByUserId(user.getId(), TransactionType.WITHDRAWAL, TransactionStatus.FAILED);
		assertTrue(transactionList.isEmpty());
	}

}
