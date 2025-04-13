package bookstore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CategoryRequestDto;
import bookstore.mapper.BookMapper;
import bookstore.mapper.CategoryMapper;
import bookstore.model.Book;
import bookstore.model.Category;
import bookstore.repository.BookRepository;
import bookstore.repository.CategoryRepository;
import bookstore.service.CategoryServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("""
            Verify the correct save category from requestDto when requestDto exists
            """)
    public void save_ValidCreateCategoryRequestDto_ReturnsCategoryDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fantasy");

        Category category = new Category();
        category.setName("Fantasy");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Fantasy");

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto savedCategoryDto = categoryService.save(requestDto);

        assertThat(savedCategoryDto).isEqualTo(categoryDto);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
            Verify findAll method works
            """)
    public void findAll_ValidMethod_ReturnsAllCategoryDtos() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        List<Category> categories = List.of(category);

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> categoryDtos = categoryService.findAll();

        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.get(0)).isEqualTo(categoryDto);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
            Verify findId method works
            """)
    public void findId_WithValidCategoryId_ReturnsCategoryDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.findById(category.getId());

        assertThat(categoryDto).isEqualTo(actual);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
            Verify getBookByCategoryId method works
            """)
    public void getBooksByCategoryId_WithValidCategoryId_ReturnsListOfBookDtoWithoutCategoryIds() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Witcher");
        book.setAuthor("Sapkovski");
        book.setIsbn("1981112314470");
        book.setPrice(BigDecimal.valueOf(30.75));
        book.getCategories().add(category);

        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);

        when(bookRepository.findAllByCategoriesId(category.getId(), pageable)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);

        List<BookDtoWithoutCategoryIds> bookDtos = categoryService
                .getBooksByCategoryId(category.getId(), pageable);

        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDto);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
