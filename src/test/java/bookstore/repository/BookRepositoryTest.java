package bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bookstore.model.Book;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/books/insert-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts =
        "classpath:database/books/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Find book by id
            """)
    void findById_ReturnsBook() {
        Book resultBook = bookRepository.findById(1L).get();
        assertEquals(1L, resultBook.getId());
    }

    @Test
    @DisplayName("""
            Find all books
            """)
    void findAll_ReturnsAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> resultBooks = bookRepository.findAll(pageable);

        assertEquals(3, resultBooks.stream().toList().size());
    }

    @Test
    @DisplayName("""
            Find all books with category id
            """)
    void findAllByCategoriesId_ReturnsAllSpecBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> resultBooks = bookRepository.findAllByCategoriesId(1L, pageable);

        assertEquals(2, resultBooks.stream().toList().size());
    }
}
