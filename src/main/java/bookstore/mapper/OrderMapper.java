package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.order.OrderDto;
import bookstore.dto.order.OrderItemsDto;
import bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);

    @Mapping(source = "orderItems", target = "orderItems")
    OrderItemsDto toItemsDto(Order order);
}
