package by.itacademy.elegantsignal.marketplace.service;

import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.OrderStatus;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.ProductType;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.RoleName;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.TransactionStatus;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.TransactionType;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IBook;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IDownload;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IGenre;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.ILike;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IOrder;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IOrderItem;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IProduct;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IReview;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IRole;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.ITransaction;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IUser;
import by.itacademy.elegantsignal.marketplace.filestorage.WrongFileTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;


@SpringJUnitConfig(locations = "classpath:service-context-test.xml")
public abstract class AbstractTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);

	@Autowired protected IBookService bookService;
	@Autowired protected ICartService cartService;
	@Autowired protected IDownloadService downloadService;
	@Autowired protected IGenreService genreService;
	@Autowired protected ILikeService likeService;
	@Autowired protected IOrderItemService orderItemService;
	@Autowired protected IOrderService orderService;
	@Autowired protected IPaymentService paymentService;
	@Autowired protected IProductService productService;
	@Autowired protected IReviewService reviewService;
	@Autowired protected IRoleService roleService;
	@Autowired protected IUserService userService;
	@Autowired protected IAccountService accountService;
	@Autowired protected ITransactionService transactionService;

	private static final Random RANDOM = new Random();

	@Value("${jdbc.url}") private String url;
	@Value("${jdbc.user}") private String user;
	@Value("${jdbc.password}") private String password;

	@BeforeEach
	public final void recreateTestDB() throws SQLException {
		final long stampBefore = System.currentTimeMillis();

		try (final Connection conn = DriverManager.getConnection(url, user, password)) {
			try (final Statement stmt = conn.createStatement()) {
				stmt.execute("DROP SCHEMA IF EXISTS \"public\" CASCADE;");
				stmt.execute("CREATE SCHEMA \"public\";");
				stmt.execute(getScript());
			}
		}

		LOGGER.info("Database recreated in {} seconds.", (double) ((System.currentTimeMillis() - stampBefore) / 1000));
	}

	private String getScript() {

		final StringBuilder contentBuilder = new StringBuilder();

		try (final Stream<String> stream = Files.lines(
			Paths.get("../docs/database/marketplace.sql"),
			StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return contentBuilder.toString();
	}

	protected String getRandomPrefix() {
		return RANDOM.nextInt(99999) + "";
	}

	protected int getRandomObjectsCount() {
		return RANDOM.nextInt(9) + 1;
	}

	protected int getRandomFromRange(final Integer max) {
		return RANDOM.nextInt(max);
	}

	public static <T extends Enum<?>> T randomEnum(final Class<T> clazz) {
		final int x = RANDOM.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	protected IUser saveNewUser(final IUser user) {
		if (user.getName() == null) {
			user.setName("User-" + getRandomPrefix());
		}

		if (user.getEmail() == null) {
			user.setEmail("email-" + getRandomPrefix());
		}

		if (user.getPassword() == null) {
			user.setPassword("password-" + getRandomPrefix());
		}

		if (user.getRole() == null || user.getRole().isEmpty()) {
			final Set<IRole> roles = new HashSet<>();
			roles.add(saveNewRole());
			user.setRole(roles);
		}

		userService.save(user);
		return user;
	}

	protected IBook saveNewBook(final IBook book) {

		if (book.getProduct() == null) {
			book.setProduct(saveNewProduct(productService.createEntity()));
		}

		if (book.getTitle() == null) {
			book.setTitle("The title#" + getRandomPrefix());
		}

		if (book.getAuthor() == null) {
			book.setAuthor("Author#" + getRandomPrefix());
		}

		if (book.getCover() == null) {
			File source = new File("../docs/seed/duke.png");
			File tmpFile = new File(System.getProperty("java.io.tmpdir") + "/duke.png");

			try {
				Files.copy(source.toPath(), tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			book.setCover(tmpFile);
		}

		if (book.getPdf() == null) {
			File source = new File("../docs/seed/dummy.pdf");
			File tmpFile = new File(System.getProperty("java.io.tmpdir") + "/dummy.pdf");

			try {
				Files.copy(source.toPath(), tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			book.setPdf(tmpFile);
		}

		if (book.getPublished() == null) {
			book.setPublished(LocalDate.now());
		}

		if (book.getDescription() == null) {
			book.setDescription("Description-" + getRandomPrefix());
		}

		try {
			return bookService.save(book);
		} catch (IOException e) {

		} catch (WrongFileTypeException e) {

		}
		return book;
	}

	protected IGenre saveNewGenre(final IGenre genre) {
		if (genre.getName() == null) {
			genre.setName("Genre-" + getRandomPrefix());
		}

		genreService.save(genre);
		return genre;
	}

	protected ILike saveNewLike() {
		final ILike entity = likeService.createEntity();
		entity.setUser(saveNewUser(userService.createEntity()));
		entity.setProduct(saveNewProduct(productService.createEntity()));
		entity.setCreated(new Date());
		likeService.save(entity);
		return entity;
	}

	protected IOrder saveNewOrder(final IOrder order) {
		if (order.getUser() == null) {
			order.setUser(saveNewUser(userService.createEntity()));
		}
		if (order.getStatus() == null) {
			order.setStatus(OrderStatus.CART);
		}
		orderService.save(order);
		return order;
	}

	protected IProduct saveNewProduct(final IProduct product) {
		if (product.getUser() == null) {
			product.setUser(saveNewUser(userService.createEntity()));
		}

		if (product.getType() == null) {
			product.setType(randomEnum(ProductType.class));
		}

		if (product.getPrice() == null) {
			product.setPrice((BigDecimal.valueOf(getRandomObjectsCount())));
		}

		productService.save(product);

		// TODO: Find bug
		//		if (product.getBook() == null) {
		//			product.setBook(saveNewBook(bookService.createEntity().setProduct(product)));
		//		}

		return productService.save(product);
	}

	protected IOrderItem saveNewOrderItem(final IOrderItem orderItem) {
		if (orderItem.getOrder() == null) {
			orderItem.setOrder(saveNewOrder(orderService.createEntity()));
		}

		if (orderItem.getProduct() == null) {
			orderItem.setProduct(saveNewProduct(productService.createEntity()));
		}

		if (orderItem.getAmount() == null) {
			orderItem.setAmount(BigDecimal.valueOf(getRandomFromRange(100)).movePointLeft(2));
		}

		orderItemService.save(orderItem);
		return orderItem;
	}

	protected IReview saveNewReview() {
		final IReview entity = reviewService.createEntity();
		entity.setOrderItem(saveNewOrderItem(orderItemService.createEntity()));
		entity.setGrade(getRandomFromRange(5));
		entity.setComment("Comment-" + getRandomPrefix());
		reviewService.save(entity);
		return entity;
	}

	protected IRole saveNewRole() {
		final IRole role = roleService.createEntity();
		role.setName(randomEnum(RoleName.class).toString());
		try {
			return roleService.getRoleByName(role.getName().toString());
		} catch (final NoResultException e) {
			return roleService.save(role);
		}
	}

	protected IDownload saveNewDownload(final IDownload download) {
		if (download.getOrderItem() == null) {
			download.setOrderItem(saveNewOrderItem(orderItemService.createEntity()));
		}
		return downloadService.save(download);
	}

	protected ITransaction saveNewTransaction(final ITransaction transaction) {
		if (transaction.getUser() == null) {
			transaction.setUser(saveNewUser(userService.createEntity()));
		}

		if (transaction.getType() == null) {
			transaction.setType(TransactionType.WITHDRAWAL);
		}

		if (transaction.getStatus() == null) {
			transaction.setStatus(TransactionStatus.SUCCESS);
		}

		return transactionService.save(transaction);
	}

}
