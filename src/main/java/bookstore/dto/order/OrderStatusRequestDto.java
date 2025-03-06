package bookstore.dto.order;

import bookstore.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusRequestDto {
    @NotNull
    private Order.Status status;
}
