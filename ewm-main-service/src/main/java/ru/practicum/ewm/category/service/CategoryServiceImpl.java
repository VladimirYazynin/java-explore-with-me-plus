package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.DataViolationException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto create(CategoryDto category) {
        log.debug("create({})", category);
        if (repository.existsByName(category.getName())) {
            throw new DataViolationException("Категория уже существует");
        }
        Category savedCategory = repository.save(mapper.toCategory(category));
        log.info("Категория создана: {}", savedCategory);
        return mapper.toCategoryDto(savedCategory);
    }

    @Override
    public CategoryDto update(Integer catId, CategoryDto category) {
        log.debug("update({}, {})", catId, category);
        Category thisCategory = repository.findByName(category.getName());
        Category foundCategory = findById(catId);
        if (Objects.nonNull(thisCategory) && !thisCategory.getId().equals(foundCategory.getId())) {
            throw new DataViolationException("Категория уже существует");
        }
        foundCategory.setName(category.getName());
        Category updatedCategory = repository.save(foundCategory);
        log.info("Категория обновлена: {}", updatedCategory);
        return mapper.toCategoryDto(updatedCategory);
    }

    @Override
    public void delete(Integer catId) {
        log.debug("delete({})", catId);
        Category thisCategory = findById(catId);
        if (eventRepository.existsByCategory(thisCategory)) {
            throw new DataViolationException("С категорией есть связанное событие");
        }
        repository.delete(thisCategory);
        log.info("Категория удалена {}", thisCategory);
    }

    @Override
    public List<CategoryDto> get(int from, int size) {
        log.debug("get({}, {})", from, size);
        PageRequest page = PageRequest.of(from, size);
        List<CategoryDto> categories = repository.findAll(page).stream().map(mapper::toCategoryDto)
                .collect(Collectors.toList());
        log.info("Получен список категорий: {}", categories);
        return categories;
    }

    @Override
    public CategoryDto getById(Integer catId) {
        log.debug("getById({})", catId);
        Category thisCategory = findById(catId);
        log.info("Возвращена категория: {}", thisCategory);
        return mapper.toCategoryDto(thisCategory);
    }

    private Category findById(Integer catId) {
        log.debug("findById({})", catId);
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        log.info("Найдена категория: {}", category);
        return category;
    }
}
