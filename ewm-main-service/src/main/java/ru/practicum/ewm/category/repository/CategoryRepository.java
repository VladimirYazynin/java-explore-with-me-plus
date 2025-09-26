package ru.practicum.ewm.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.category.model.Category;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, QuerydslPredicateExecutor<Category> {
    boolean existsByName(String name);

    Category findByName(String name);

    List<Category> findAllByIdIn(Collection<Integer> id);
}
