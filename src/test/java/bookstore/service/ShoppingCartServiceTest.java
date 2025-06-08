package bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.CartItemMapper;
import bookstore.mapper.ShoppingCartMapper;
import bookstore.model.Book;
import bookstore.model.CartItem;
import bookstore.model.ShoppingCart;
import bookstore.model.User;
import bookstore.repository.BookRepository;
import bookstore.repository.CartItemRepository;
import bookstore.repository.ShoppingCartRepository;
import bookstore.util.ShoppingCartTestUtilClass;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private final ShoppingCartTestUtilClass testUtil = new ShoppingCartTestUtilClass();

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

    @Test
    @DisplayName("""
            Verify add cart item to shopping cart
            """)
    public void addCartItem_ValidCartItemRequestDto_ReturnsShoppingCartDto() {
        User user = testUtil.createTestUser();
        Book book = testUtil.createTestBook();
        CartItemRequestDto requestDto = testUtil.createTestCartItemRequestDto();
        CartItem cartItem = testUtil.createTestCartItem();

        ShoppingCart shoppingCart = testUtil.createTestShoppingCart();
        ShoppingCartDto shoppingCartDto = testUtil.createTestShoppingCartDto();

        when(authentication.getPrincipal()).thenReturn(user);
        when(cartItemRepository.findByShoppingCartIdWhereBookId(
                user.getId(), requestDto.getBookId()))
                .thenReturn(null);
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

    @Test
    @DisplayName("""
            Verify add cart item to shopping cart when cart item already exists
            """)
    public void addCartItem_CartItemAlreadyExists_ReturnsShoppingCartDto() {
        User user = testUtil.createTestUser();
        CartItemRequestDto requestDto = testUtil.createTestCartItemRequestDto();
        CartItem cartItem = testUtil.createTestCartItem();
        ShoppingCart shoppingCart = testUtil.createTestShoppingCart();
        ShoppingCartDto shoppingCartDto = testUtil.createTestShoppingCartDto();

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

    @Test
    @DisplayName("""
            Verify find cart item in shopping cart
            """)
    public void find_ValidAuthentication_ReturnsShoppingCartDto() {
        User user = testUtil.createTestUser();
        ShoppingCart shoppingCart = testUtil.createTestShoppingCart();
        ShoppingCartDto shoppingCartDto = testUtil.createTestShoppingCartDto();

        when(authentication.getPrincipal()).thenReturn(user);
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.find(authentication);

        assertThat(actual).isEqualTo(shoppingCartDto);
        verifyNoMoreInteractions(shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("""
            Verify update cart item to shopping cart
            """)
    public void updateCartItemById_ValidCartItem_ReturnsShoppingCartDto() {
        Long id = 1L;
        int quantity = 2;
        User user = testUtil.createTestUser();
        CartItem cartItem = testUtil.createTestCartItem();
        CartItem updatedCartItem = testUtil.createTestCartItem();
        updatedCartItem.setQuantity(quantity);
        ShoppingCart shoppingCart = testUtil.createTestShoppingCart();
        ShoppingCartDto shoppingCartDto = testUtil.createTestShoppingCartDto();

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

    @Test
    @DisplayName("""
            Verify update cart item to shopping cart invalid cart item id
            """)
    public void updateCartItemById_InvalidCartItemId_ReturnsException() {
        User user = testUtil.createTestUser();
        Long wrongId = 2L;
        int quantity = 2;

        when(authentication.getPrincipal()).thenReturn(user);
        when(cartItemRepository.findById(wrongId))
                .thenThrow(new EntityNotFoundException("Can't find cart item with id: " + wrongId));

        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.updateCartItemById(wrongId, quantity, authentication));
    }
}
