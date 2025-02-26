package bookstore.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartRequestDto {
    @NotNull
    private Long userId;
    private List<Long> cartItemsIds;
}
