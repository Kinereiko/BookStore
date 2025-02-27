package bookstore.service;

import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto addCartItem(CartItemRequestDto requestDto, Authentication authentication);

    ShoppingCartDto find(Authentication authentication);

    ShoppingCartDto updateCartItemById(Long id, int quantity, Authentication authentication);

    void deleteCartItemById(Long id);

    void createDefaultShoppingCart(User user);

    //User getAuthenticatedUser();
}
