package bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.util.ShoppingCartTestUtilClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private final ShoppingCartTestUtilClass testUtil = new ShoppingCartTestUtilClass();
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Add cart item")
    @WithUserDetails(value = "test@gmail.com")
    @Sql(scripts = "classpath:database/books/insert-data.sql")
    @Sql(scripts =
            "classpath:database/books/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ValidRequestDto_Success() throws Exception {
        CartItemRequestDto requestDto = testUtil.createTestCartItemRequestDto();

        ShoppingCartDto expected = testUtil.createTestShoppingCartDto();
        expected.getCartItems().get(0).setQuantity(2);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);

        String expectedJson = objectMapper.writeValueAsString(expected);
        String actualJson = objectMapper.writeValueAsString(actual);
        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

    @Test
    @DisplayName("Add cart item")
    @WithUserDetails(value = "test@gmail.com")
    @Sql(scripts = "classpath:database/books/insert-data.sql")
    @Sql(scripts =
            "classpath:database/books/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_CartItemDtoIsNull_ReturnsBadRequest() throws Exception {
        CartItemRequestDto requestDto = null;

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        int actual = result.getResponse().getStatus();
        int expected = 400;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Find shopping cart")
    @WithUserDetails(value = "test@gmail.com")
    @Sql(scripts = "classpath:database/books/insert-data.sql")
    @Sql(scripts =
            "classpath:database/books/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void find_ValidAuthentication_Success() throws Exception {
        ShoppingCartDto expected = testUtil.createTestShoppingCartDto();

        MvcResult result = mockMvc.perform(get("/cart")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "cartItems"));
    }
}
