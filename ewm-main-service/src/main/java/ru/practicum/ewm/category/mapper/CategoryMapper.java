package ru.practicum.ewm.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryDto category) {
        return new Category(
                category.getId(),
                category.getName());
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName());
    }
}
