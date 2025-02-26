package bookstore.dto.cartItem;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateRequestDto {
    @Min(1)
    int quantity;
}
