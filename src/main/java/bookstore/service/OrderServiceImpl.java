package bookstore.service;

import bookstore.dto.order.OrderDto;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    @Override
    public List<OrderDto> findAll(Pageable pageable, Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return orderMapper.toOrderDtoList(orderRepository.findAllByUserId(userId, pageable));
    }

    @Override
    public OrderDto save(OrderRequestDto requestDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        if (shoppingCart.getCartItems() == null) {
            throw new NullPointerException("Shopping cart is empty");
        }
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

        orderRepository.save(order);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemDto> getOrderItemsById(Long id) {
        return orderItemMapper.toDtoList(orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order with id: "
                        + id)).getOrderItems().stream().toList());
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long orderItemId,
                                         Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return orderItemMapper.toDto(orderItemRepository
                .findByIdAndOrderIdAndUserId(orderId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item with id: "
                        + orderItemId + " and order id: " + orderId + " and user id: " + userId)));
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
