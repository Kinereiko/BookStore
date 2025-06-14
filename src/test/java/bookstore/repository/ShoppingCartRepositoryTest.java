package bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bookstore.model.ShoppingCart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/books/insert-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts =
        "classpath:database/books/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("""
            Find shopping cart by id
            """)
    void findByUserId_ReturnsShoppingCart() {
        ShoppingCart resultShoppingCart = shoppingCartRepository.findByUserId(1L);
        assertEquals(1L, resultShoppingCart.getId());
    }
}
