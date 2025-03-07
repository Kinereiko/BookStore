package bookstore.service;

import bookstore.dto.order.OrderDto;
import bookstore.dto.order.OrderItemsDto;
import bookstore.dto.order.OrderRequestDto;
import bookstore.dto.order.OrderStatusRequestDto;
import bookstore.dto.orderitem.OrderItemDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.OrderItemMapper;
import bookstore.mapper.OrderMapper;
import bookstore.model.Order;
import bookstore.model.OrderItem;
import bookstore.model.ShoppingCart;
import bookstore.model.User;
import bookstore.repository.OrderItemRepository;
import bookstore.repository.OrderRepository;
import bookstore.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final Order.Status defaultStatus = Order.Status.PENDING;

    @Override
    public List<OrderDto> findAll(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orderRepository.findAllByUserId(user.getId())) {
            orderDtos.add(orderMapper.toDto(order));
        }
        return orderDtos;
    }

    @Override
    public OrderDto save(OrderRequestDto requestDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Order order = new Order();
        order.setUser(user);
        order.setStatus(defaultStatus);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        for (var cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());

            total = orderItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            orderItems.add(orderItem);
        }

        order.setTotal(total);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderItemsDto getOrderItemsById(Long id) {
        return orderMapper.toItemsDto(orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order with id: "
                        + id)));
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long id) {
        return orderItemMapper.toDto(orderItemRepository.findByIdAndOrderId(id, orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item with id: "
                        + id + " and order id: " + orderId)));
    }

    @Override
    public void updateStatus(Long id, OrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order with id: "
                + id));
        order.setStatus(requestDto.getStatus());
        orderRepository.save(order);
    }
}
