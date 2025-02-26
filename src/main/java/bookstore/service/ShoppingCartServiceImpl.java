package bookstore.service;

import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.CartItemMapper;
import bookstore.mapper.ShoppingCartMapper;
import bookstore.model.CartItem;
import bookstore.model.ShoppingCart;
import bookstore.model.User;
import bookstore.repository.BookRepository;
import bookstore.repository.CartItemRepository;
import bookstore.repository.ShoppingCartRepository;
import bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto addCartItem(CartItemRequestDto requestDto) {
        User user = getAuthenticatedUser();
        CartItem cartItemExist = cartItemRepository.findByShoppingCartIdWhereBookId(user.getId(),
                requestDto.getBookId());
        if (cartItemExist != null) {
            return updateCartItemById(cartItemExist.getId(),
                    cartItemExist.getQuantity() + requestDto.getQuantity());
        }
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCartRepository.findByUserId(user.getId()));
        cartItem.setBook(bookRepository.getReferenceById(requestDto.getBookId()));
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto find() {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(getAuthenticatedUser().getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateCartItemById(Long id, int quantity) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item with id: "
                        + id));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(getAuthenticatedUser().getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteCartItemById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void createDefaultShoppingCart(String email) {
        User user = userRepository.findWithRolesByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with email: "
                        + email));
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return userRepository.findWithRolesByEmail(((UserDetails) authentication.getPrincipal())
                    .getUsername()).orElseThrow(()
                            -> new EntityNotFoundException("Can't find authenticated user"));
        }
        return null;
    }
}
