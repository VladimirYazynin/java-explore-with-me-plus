package ru.practicum.ewm.category;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.dto.CategoryDto;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category mapToCategory() {
        return null;
    }

    @Override
    public CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
