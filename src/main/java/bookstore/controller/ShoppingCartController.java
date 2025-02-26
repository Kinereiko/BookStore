package bookstore.controller;

import bookstore.dto.cartItem.CartItemRequestDto;
import bookstore.dto.cartItem.CartItemUpdateRequestDto;
import bookstore.dto.shoppingCart.ShoppingCartDto;
import bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get the shopping cart", description = "Get the shopping cart by id")
    public ShoppingCartDto find() {
        return shoppingCartService.find();
    }

    @PostMapping
    @Operation(summary = "Add a book", description = "Add book to the shopping cart")
    public ShoppingCartDto save(@RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addCartItem(requestDto);
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "Update the cart item", description = "Update the cart item by id")
    public ShoppingCartDto update(@PathVariable Long id,
                                  @RequestBody @Valid CartItemUpdateRequestDto updateDto) {
        return shoppingCartService.updateCartItemById(id, updateDto.getQuantity());
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete the cart item", description = "Delete the cart item by id")
    public void delete(@PathVariable Long id) {
        shoppingCartService.deleteCartItemById(id);
    }
}
