package bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.CreateBookRequestDto;
import bookstore.mapper.BookMapper;
import bookstore.model.Book;
import bookstore.model.Category;
import bookstore.repository.BookRepository;
import bookstore.repository.CategoryRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("""
            Verify the correct save book from requestDto when requestDto exists
            """)
    public void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        Category category = createTestCategory();

        Book book = createTestBook();

        CreateBookRequestDto requestDto = createTestBookRequestDto(book);
        requestDto.getCategoryIds().add(category.getId());

        BookDto bookDto = createTestBookDto(book);
        bookDto.getCategoryIds().add(category.getId());

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(categoryRepository.getReferenceById(category.getId())).thenReturn(category);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBookDto = bookService.save(requestDto);

        assertThat(savedBookDto).isEqualTo(bookDto);
        verifyNoMoreInteractions(categoryRepository, bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Verify the throw exception when request dto is null
            """)
    public void save_BookRequestDtoIsNull_ShouldThrowException() {
        CreateBookRequestDto requestDto = null;

        assertThrows(NullPointerException.class,
                () -> bookService.save(requestDto));
    }

    @Test
    @DisplayName("""
            Verify the correct save book from requestDto when requestDto exists
            """)
    public void findAll_ValidMethod_ReturnsAllBookDtos() {
        Category category = createTestCategory();

        Book book = createTestBook();

        BookDto bookDto = createTestBookDto(book);
        bookDto.getCategoryIds().add(category.getId());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtos = bookService.findAll(pageable);

        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDto);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Verify the correct book dto was returned when book exists
            """)
    public void findById_ValidMethod_ReturnsBookDto() {
        Category category = createTestCategory();

        Book book = createTestBook();

        BookDto bookDto = createTestBookDto(book);
        bookDto.getCategoryIds().add(category.getId());

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.findById(book.getId());

        assertThat(actual).isEqualTo(bookDto);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Verify the throw Exception when don't existing book id
            """)
    public void findById_WithNoneExistingBookId_ShouldThrowException() {
        Long bookId = 100L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                bookService.findById(bookId));

        String expected = "Can't find book with id: " + bookId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");
        return category;
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Witcher");
        book.setAuthor("Sapkovsky");
        book.setIsbn("1981112314470");
        book.setPrice(BigDecimal.valueOf(30.75));
        Set<Category> categories = new HashSet<>();
        return book;
    }

    private CreateBookRequestDto createTestBookRequestDto(Book book) {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(book.getTitle());
        requestDto.setAuthor(book.getAuthor());
        requestDto.setIsbn(book.getIsbn());
        requestDto.setPrice(book.getPrice());
        requestDto.setCategoryIds(new ArrayList<>());
        return requestDto;
    }

    private BookDto createTestBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setCategoryIds(new ArrayList<>());
        return bookDto;
    }
}
