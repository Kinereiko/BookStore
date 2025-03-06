package bookstore.service;

import bookstore.dto.order.OrderDto;
import bookstore.dto.order.OrderItemsDto;
import bookstore.dto.order.OrderRequestDto;
import bookstore.dto.order.OrderStatusRequestDto;
import bookstore.dto.orderitem.OrderItemDto;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface OrderService {
    List<OrderDto> findAll(Authentication authentication);

    OrderDto save(OrderRequestDto requestDto, Authentication authentication);

    OrderItemsDto getOrderItemsById(Long id);

    OrderItemDto getOrderItemById(Long orderId, Long id);

    void updateStatus(Long id, OrderStatusRequestDto requestDto);
}
