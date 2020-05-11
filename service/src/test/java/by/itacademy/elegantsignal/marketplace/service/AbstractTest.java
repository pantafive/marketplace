package by.itacademy.elegantsignal.marketplace.service;

import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.OrderStatus;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.ProductType;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.enums.RoleName;
import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.*;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;


@SpringJUnitConfig(locations = "classpath:service-context-test.xml")
public abstract class AbstractTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);

	@Autowired
	protected IUserService userService;

	@Autowired
	protected IProductService productService;

	@Autowired
	protected IBookService bookService;

	@Autowired
	protected IGenreService genreService;

	@Autowired
	protected ILikeService likeService;

	@Autowired
	protected IOrderService orderService;

	@Autowired
	protected IOrderItemService orderItemService;

	@Autowired
	protected IReviewService reviewService;

	@Autowired
	protected IRoleService roleService;

	private static final Random RANDOM = new Random();

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.user}")
	private String user;

	@Value("${jdbc.password}")
	private String password;

	@BeforeEach
	public final void recreateTestDB() throws SQLException, IOException {
		final long stampBefore = System.currentTimeMillis();

		final Connection conn = DriverManager.getConnection(url, user, password);

		try {
			final Statement stmt = conn.createStatement();
			try {
				stmt.execute("DROP SCHEMA IF EXISTS \"public\" CASCADE;");
				stmt.execute("CREATE SCHEMA \"public\";");
				stmt.execute(getScript());
			} finally {
				stmt.close();
			}
		} finally {
			conn.close();
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

	public Random getRANDOM() {
		return RANDOM;
	}

	public static <T extends Enum<?>> T randomEnum(final Class<T> clazz) {
		final int x = RANDOM.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	protected IUser saveNewUser() {
		final IUser user = userService.createEntity();
		user.setName("User-" + getRandomPrefix());
		user.setEmail("email-" + getRandomPrefix());
		user.setPassword("password-" + getRandomPrefix());
		userService.save(user);
		return user;
	}

	protected IUser saveNewUser(final Set<IRole> roleSet) {
		final IUser user = userService.createEntity();
		user.setName("User-" + getRandomPrefix());
		user.setEmail("email-" + getRandomPrefix());
		user.setPassword("password-" + getRandomPrefix());
		user.setRole(roleSet);
		userService.save(user);
		return user;
	}

	protected IBook saveNewBook(final IBook book) {
		return saveNewBook(saveNewProduct(), book);
	}

	protected IBook saveNewBook(final IProduct product, final IBook book) {

		if (book.getProduct() == null) {
			book.setProduct(product);
		}

		if (book.getTitle() == null) {
			book.setTitle("The title#" + getRandomPrefix());
		}

		if (book.getCover() == null) {
			book.setCover(new File("/tmp/" + getRandomPrefix()));
		}

		if (book.getPublished() == null) {
			book.setPublished(LocalDate.now());
		}

		if (book.getDescription() == null) {
			book.setDescription("Description-" + getRandomPrefix());
		}

		if (book.getPdf() == null) {
			book.setPdf(new File("/testpdf/" + getRandomPrefix()));
		}

		try {
			bookService.save(book);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return book;
	}

	protected IGenre saveNewGenre() {
		final IGenre entity = genreService.createEntity();
		entity.setName("Genre-" + getRandomPrefix());
		genreService.save(entity);
		return entity;
	}

	protected ILike saveNewLike() {
		final ILike entity = likeService.createEntity();
		entity.setUser(saveNewUser());
		entity.setProduct(saveNewProduct());
		entity.setCreated(new Date());
		likeService.save(entity);
		return entity;
	}

	protected IOrder saveNewOrder(final IOrder order) {
		if (order.getUser() == null) {
			order.setUser(this.saveNewUser());
		}
		if (order.getStatus() == null) {
			order.setStatus(OrderStatus.CART);
		}
		orderService.save(order);
		return order;
	}

	protected IProduct saveNewProduct() {
		final IUser user = saveNewUser();
		return saveNewProduct(user);
	}

	protected IProduct saveNewProduct(final IUser user) {
		final IProduct product = productService.createEntity();
		product.setUser(user);
		product.setType(randomEnum(ProductType.class));
		product.setPrice((BigDecimal.valueOf(getRandomObjectsCount())));
		productService.save(product);
		return product;
	}

	protected IOrderItem saveNewOrderItem(final IOrderItem orderItem) {
		if (orderItem.getOrder() == null) {
			orderItem.setOrder(saveNewOrder(orderService.createEntity()));
		}

		if (orderItem.getProduct() == null) {
			orderItem.setProduct(saveNewProduct());
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
		roleService.save(role);
		return role;
	}
}
