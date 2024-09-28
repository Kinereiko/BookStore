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

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book witcher = new Book();
                witcher.setTitle("Witcher");
                witcher.setAuthor("Mj Sabkovsky");
                witcher.setPrice(BigDecimal.valueOf(230));

                bookService.save(witcher);
                System.out.println(bookService.findAll());
            }
        };
    }
}
