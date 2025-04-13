package bookstore;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.CreateBookRequestDto;
import bookstore.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
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

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book")
    @Sql(scripts =
            "classpath:database/books/delete-witcher-book-by-title.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Witcher");
        requestDto.setAuthor("Sapkovsky");
        requestDto.setIsbn("1981112314470");
        requestDto.setPrice(BigDecimal.valueOf(30.75));
        requestDto.setCategoryIds(new ArrayList<>());
        requestDto.getCategoryIds().add(category.getId());

        BookDto expected = new BookDto();
        expected.setTitle("Witcher");
        expected.setAuthor("Sapkovsky");
        expected.setIsbn("1981112314470");
        expected.setPrice(BigDecimal.valueOf(30.75));
        expected.setCategoryIds(new ArrayList<>());
        expected.getCategoryIds().add(category.getId());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get all products")
    @Sql(scripts =
            "classpath:database/books/add-multiple-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts =
            "classpath:database/books/delete-all-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_GivenBooksInCatalog_ReturnsAllBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.length);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get book by id")
    @Sql(scripts = "classpath:database/books/add-witcher-book-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts =
            "classpath:database/books/delete-witcher-book-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ValidRequestDto_ReturnsBookById() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Witcher");
        expected.setAuthor("Sapkovsky");
        expected.setIsbn("1981112314470");
        expected.setPrice(BigDecimal.valueOf(30.75));
        expected.setCategoryIds(new ArrayList<>());
        expected.getCategoryIds().add(category.getId());

        MvcResult result = mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
