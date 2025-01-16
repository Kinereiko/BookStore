package bookstore.service;

import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CategoryRequestDto;
import bookstore.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto save(CategoryRequestDto requestDto);

    List<CategoryDto> findAll();

    CategoryDto findById(Long id);

    void deleteById(Long id);

    CategoryDto update(Long id, CategoryRequestDto requestDto);
}
