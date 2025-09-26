package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto category);

    CategoryDto update(Integer catId, CategoryDto category);

    void delete(Integer catId);

    List<CategoryDto> get(int from, int size);

    CategoryDto getById(Integer catId);
}
