package bookstore.service;

import bookstore.dto.cartItem.CartItemRequestDto;
import bookstore.dto.shoppingCart.ShoppingCartDto;
import bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto addCartItem(CartItemRequestDto requestDto);

    ShoppingCartDto find();

    ShoppingCartDto updateCartItemById(Long id, int quantity);

    void deleteCartItemById(Long id);

    void createDefaultShoppingCart(String email);

    User getAuthenticatedUser();
}
