package bookstore.controller;

import bookstore.dto.order.OrderDto;
import bookstore.dto.order.OrderItemsDto;
import bookstore.dto.order.OrderRequestDto;
import bookstore.dto.order.OrderStatusRequestDto;
import bookstore.dto.orderitem.OrderItemDto;
import bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders", description = "Get a list of all user orders")
    public List<OrderDto> findAll(Authentication authentication) {
        return orderService.findAll(authentication);
    }

    @PostMapping
    @Operation(summary = "Create an order", description = "Create a new order")
    public OrderDto save(@RequestBody @Valid OrderRequestDto requestDto,
                         Authentication authentication) {
        return orderService.save(requestDto, authentication);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items",
            description = "Get order items of specific order by id")
    public OrderItemsDto getOrderItemsById(@PathVariable Long orderId) {
        return orderService.getOrderItemsById(orderId);
    }

    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Get order item", description = "Get order item by id from order where id")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId,
                                         @PathVariable Long id) {
        return orderService.getOrderItemById(orderId, id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update order status", description = "Update order status by id")
    public void updateStatus(@PathVariable Long id,
                             @RequestBody @Valid OrderStatusRequestDto requestDto) {
        orderService.updateStatus(id, requestDto);
    }
}
