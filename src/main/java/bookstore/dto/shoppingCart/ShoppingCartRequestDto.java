package bookstore.dto.shoppingCart;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ShoppingCartRequestDto {
    @NotNull
    private Long userId;
    private List<Long> cartItemsIds;
}
