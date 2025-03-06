package bookstore.dto.order;

import bookstore.dto.orderitem.OrderItemDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsDto {
    private List<OrderItemDto> orderItems;
}
