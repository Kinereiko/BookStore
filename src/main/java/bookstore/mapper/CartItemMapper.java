package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.cartItem.CartItemDto;
import bookstore.dto.cartItem.CartItemRequestDto;
import bookstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemDto toDto(CartItem item);

    CartItem toModel(CartItemRequestDto requestDto);

    @AfterMapping
    default void setBookIdAndBookTitle(@MappingTarget CartItemDto cartItemDto,
                                       CartItem cartItem) {
        if (cartItem.getBook().getId() != null
                && cartItem.getBook().getTitle() != null) {
            cartItemDto.setBookId(cartItem.getBook().getId());
            cartItemDto.setBookTitle(cartItem.getBook().getTitle());
        }
    }
}
