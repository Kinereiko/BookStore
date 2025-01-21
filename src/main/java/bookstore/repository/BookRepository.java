package bookstore.repository;

import bookstore.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book>, PagingAndSortingRepository<Book, Long> {
    @EntityGraph(attributePaths = {"categories"})
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"categories"})
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    List<Book> findAll(@Nullable Specification<Book> spec);

    //@EntityGraph(attributePaths = {"categories"})
    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoriesId(Long categoryId);
}
