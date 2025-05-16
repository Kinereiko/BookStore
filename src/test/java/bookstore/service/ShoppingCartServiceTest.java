package bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import bookstore.dto.cartitem.CartItemDto;
import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.CartItemMapper;
import bookstore.mapper.ShoppingCartMapper;
import bookstore.model.Book;
import bookstore.model.CartItem;
import bookstore.model.Category;
import bookstore.model.ShoppingCart;
import bookstore.model.User;
import bookstore.repository.BookRepository;
import bookstore.repository.CartItemRepository;
import bookstore.repository.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private Authentication authentication;

    @WithMockUser(username = "testUser")
    @Test
    @DisplayName("""
            Verify add cart item to shopping cart
            """)
    public void addCartItem_ValidCartItemRequestDto_ReturnsShoppingCartDto() {
        User user = createTestUser();
        Book book = createTestBook();
        CartItemRequestDto requestDto = createTestCartItemRequestDto();
        CartItem cartItem = createTestCartItem();
        CartItem nullCartItem = null;
        ShoppingCart shoppingCart = createTestShoppingCart();
        ShoppingCartDto shoppingCartDto = createTestShoppingCartDto();

        when(authentication.getPrincipal()).thenReturn(user);

        when(cartItemRepository.findByShoppingCartIdWhereBookId(
                user.getId(), requestDto.getBookId()))
                .thenReturn(nullCartItem);

        when(cartItemMapper.toModel(requestDto)).thenReturn(cartItem);

        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);

        when(bookRepository.getReferenceById(requestDto.getBookId())).thenReturn(book);

        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.addCartItem(requestDto, authentication);

        assertThat(actual).isEqualTo(shoppingCartDto);
        verifyNoMoreInteractions(cartItemRepository, shoppingCartRepository, bookRepository,
                shoppingCartMapper, cartItemMapper);
    }

    @WithMockUser(username = "testUser")
    @Test
    @DisplayName("""
            Verify add cart item to shopping cart when cart item already exists
            """)
    public void addCartItem_CartItemAlreadyExists_ReturnsShoppingCartDto() {
        User user = createTestUser();
        CartItemRequestDto requestDto = createTestCartItemRequestDto();
        CartItem cartItem = createTestCartItem();
        ShoppingCart shoppingCart = createTestShoppingCart();
        ShoppingCartDto shoppingCartDto = createTestShoppingCartDto();

        when(authentication.getPrincipal()).thenReturn(user);

        when(cartItemRepository.findByShoppingCartIdWhereBookId(
                user.getId(), requestDto.getBookId()))
                .thenReturn(cartItem);

        when(authentication.getPrincipal()).thenReturn(user);

        when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));

        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);

        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.addCartItem(requestDto, authentication);

        assertThat(actual).isEqualTo(shoppingCartDto);
    }

    @WithMockUser(username = "testUser")
    @Test
    @DisplayName("""
            Verify find cart item in shopping cart
            """)
    public void find_ValidAuthentication_ReturnsShoppingCartDto() {
        User user = createTestUser();
        ShoppingCart shoppingCart = createTestShoppingCart();
        ShoppingCartDto shoppingCartDto = createTestShoppingCartDto();

        when(authentication.getPrincipal()).thenReturn(user);

        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);

        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.find(authentication);

        assertThat(actual).isEqualTo(shoppingCartDto);
        verifyNoMoreInteractions(shoppingCartRepository, shoppingCartMapper);
    }

    @WithMockUser(username = "testUser")
    @Test
    @DisplayName("""
            Verify update cart item to shopping cart
            """)
    public void updateCartItemById_ValidCartItem_ReturnsShoppingCartDto() {
        Long id = 1L;
        int quantity = 2;
        User user = createTestUser();
        CartItem cartItem = createTestCartItem();
        CartItem updatedCartItem = createTestCartItem();
        updatedCartItem.setQuantity(quantity);
        ShoppingCart shoppingCart = createTestShoppingCart();
        ShoppingCartDto shoppingCartDto = createTestShoppingCartDto();

        when(authentication.getPrincipal()).thenReturn(user);

        when(cartItemRepository.findById(id)).thenReturn(Optional.of(cartItem));

        when(cartItemRepository.save(cartItem)).thenReturn(updatedCartItem);

        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);

        when(shoppingCartMapper.toDto(shoppingCart))
                .thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService
                .updateCartItemById(id, quantity, authentication);

        assertThat(actual).isEqualTo(shoppingCartDto);
        verifyNoMoreInteractions(cartItemRepository, shoppingCartRepository, shoppingCartMapper);
    }

    @WithMockUser(username = "testUser")
    @Test
    @DisplayName("""
            Verify update cart item to shopping cart invalid cart item id
            """)
    public void updateCartItemById_InvalidCartItemId_ReturnsException() {
        User user = createTestUser();
        Long wrongId = 2L;
        int quantity = 2;

        when(authentication.getPrincipal()).thenReturn(user);

        when(cartItemRepository.findById(wrongId))
                .thenThrow(new EntityNotFoundException("Can't find cart item with id: " + wrongId));

        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.updateCartItemById(wrongId, quantity, authentication));
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setPassword("test1pass");
        user.setFirstName("First");
        user.setLastName("Last");
        return user;
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Witcher");
        book.setAuthor("Sapkovsky");
        book.setIsbn("1981112314470");
        book.setPrice(BigDecimal.valueOf(30.75));
        Set<Category> categories = new HashSet<>();
        return book;
    }

    private CartItem createTestCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setShoppingCart(new ShoppingCart());
        cartItem.setBook(createTestBook());
        cartItem.setQuantity(1);
        return cartItem;
    }

    private CartItemRequestDto createTestCartItemRequestDto() {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(1);
        return cartItemRequestDto;
    }

    private CartItemDto createTestCartItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setBookId(1L);
        cartItemDto.setBookTitle("Witcher");
        cartItemDto.setQuantity(1);
        return cartItemDto;
    }

    private ShoppingCart createTestShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(createTestUser());
        return shoppingCart;
    }

    private ShoppingCartDto createTestShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);
        shoppingCartDto.setCartItems(List.of(createTestCartItemDto()));
        return shoppingCartDto;
    }
}
