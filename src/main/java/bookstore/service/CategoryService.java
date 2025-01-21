package bookstore.service;

import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CategoryRequestDto requestDto);

    List<CategoryDto> findAll();

    CategoryDto findById(Long id);

    void deleteById(Long id);

    CategoryDto update(Long id, CategoryRequestDto requestDto);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);
}
