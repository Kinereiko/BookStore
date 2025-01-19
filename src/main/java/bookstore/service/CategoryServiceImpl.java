package bookstore.service;

import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.category.CategoryDto;
import bookstore.dto.category.CategoryRequestDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.BookMapper;
import bookstore.mapper.CategoryMapper;
import bookstore.model.Category;
import bookstore.repository.BookRepository;
import bookstore.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public CategoryDto save(CategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find category with id: "
                        + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        category.setId(id);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookRepository.findAllByCategories_Id(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
