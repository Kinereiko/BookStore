package bookstore;

import bookstore.model.Book;
import bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

/*
    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("Sample Book 1");
                book.setAuthor("Author A");
                book.setIsbn("9781234567897");
                book.setPrice(BigDecimal.valueOf(19.99));
                book.setDescription("This is a sample book description.");
                book.setCoverImage("http://example.com/cover1.jpg");

                Book book2 = new Book();
                book2.setTitle("Sample Book 2");
                book2.setAuthor("Author B");
                book2.setIsbn("9789876543210");
                book2.setPrice(BigDecimal.valueOf(24.99));
                book2.setDescription("Another sample book description.");
                book2.setCoverImage("http://example.com/cover2.jpg");

                bookService.save(book);
                bookService.save(book2);
            }
        };
    }
    */
}
