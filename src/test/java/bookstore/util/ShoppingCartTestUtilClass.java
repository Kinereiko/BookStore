package bookstore.util;

import bookstore.dto.cartitem.CartItemDto;
import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.model.Book;
import bookstore.model.CartItem;
import bookstore.model.Category;
import bookstore.model.ShoppingCart;
import bookstore.model.User;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingCartTestUtilClass {
    public static User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setPassword("test1pass");
        user.setFirstName("First");
        user.setLastName("Last");
        return user;
    }

    public static Book createTestBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Witcher");
        book.setAuthor("Sapkovsky");
        book.setIsbn("1981112314470");
        book.setPrice(BigDecimal.valueOf(30.75));
        Set<Category> categories = new HashSet<>();
        return book;
    }

    public static CartItem createTestCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setShoppingCart(new ShoppingCart());
        cartItem.setBook(createTestBook());
        cartItem.setQuantity(1);
        return cartItem;
    }

    public static CartItemRequestDto createTestCartItemRequestDto() {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(1);
        return cartItemRequestDto;
    }

    public static CartItemDto createTestCartItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setBookId(1L);
        cartItemDto.setBookTitle("Witcher");
        cartItemDto.setQuantity(1);
        return cartItemDto;
    }

    public static ShoppingCart createTestShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(createTestUser());
        return shoppingCart;
    }

    public static ShoppingCartDto createTestShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);
        shoppingCartDto.setCartItems(List.of(createTestCartItemDto()));
        return shoppingCartDto;
    }
}
