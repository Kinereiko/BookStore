package bookstore.repository;

import bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci "
            + "WHERE ci.shoppingCart.id = :shoppingCartId AND ci.book.id = :bookId")
    CartItem findByShoppingCartIdWhereBookId(Long shoppingCartId,
                                             Long bookId);
}
