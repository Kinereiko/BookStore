package bookstore.service;

import bookstore.dto.order.OrderDto;
import bookstore.dto.order.OrderRequestDto;
import bookstore.dto.order.OrderStatusRequestDto;
import bookstore.dto.orderitem.OrderItemDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    List<OrderDto> findAll(Pageable pageable, Authentication authentication);

    OrderDto save(OrderRequestDto requestDto, Authentication authentication);

    List<OrderItemDto> getOrderItemsById(Long id);

    OrderItemDto getOrderItemById(Long orderId, Long orderItemId, Authentication authentication);

    OrderDto updateStatus(Long id, OrderStatusRequestDto requestDto);
}
