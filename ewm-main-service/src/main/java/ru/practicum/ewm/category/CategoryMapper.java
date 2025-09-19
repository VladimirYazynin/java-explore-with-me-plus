package ru.practicum.ewm.category;

import ru.practicum.ewm.category.dto.CategoryDto;

public interface CategoryMapper {

    Category mapToCategory();

    CategoryDto mapToCategoryDto(Category category);

}
