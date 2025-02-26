package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.cartitem.CartItemDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.dto.shoppingcart.ShoppingCartRequestDto;
import bookstore.model.ShoppingCart;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(ShoppingCartRequestDto requestDto);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartDto cartDto,
                           ShoppingCart shoppingCart) {
        if (shoppingCart.getUser() != null && shoppingCart.getUser().getId() != null) {
            cartDto.setUserId(shoppingCart.getUser().getId());
        }
    }

    @AfterMapping
    default void setCartItemDtos(@MappingTarget ShoppingCartDto cartDto,
                                 ShoppingCart shoppingCart) {
        if (cartDto.getCartItems() == null) {
            cartDto.setCartItems(new ArrayList<>());
        }
        if (shoppingCart.getCartItems() != null) {
            List<CartItemDto> sortedCartItems = shoppingCart.getCartItems().stream()
                    .map(cartItem -> {
                        CartItemDto cartItemDto = new CartItemDto();
                        cartItemDto.setId(cartItem.getId());
                        cartItemDto.setBookId(cartItem.getBook().getId());
                        cartItemDto.setBookTitle(cartItem.getBook().getTitle());
                        cartItemDto.setQuantity(cartItem.getQuantity());
                        return cartItemDto;
                    })
                    .sorted(Comparator.comparing(CartItemDto::getId))
                    .collect(Collectors.toList());

            cartDto.setCartItems(sortedCartItems);
        }
    }
}
