package bookstore.repository;

import bookstore.model.Order;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends JpaRepository<Order, Long>,
        PagingAndSortingRepository<Order, Long> {
    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.book"})
    List<Order> findAllByUserId(Long id, Pageable pageable);
}
