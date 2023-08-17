package com.codev.domain.service;

import com.codev.domain.dto.form.CategoryDTOForm;
import com.codev.domain.model.Category;
import com.codev.domain.repository.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAllCategories() {
        return categoryRepository.findAllCategories();
    }

    @Transactional
    public Category createCategory(CategoryDTOForm categoryDTOForm) {
        Category category = new Category(categoryDTOForm.getName());
        category.persist();
        return category;
    }

    @Transactional
    public Category updateCategory(UUID categoryId, CategoryDTOForm categoryDTOForm) {
        Category category = Category.findById(categoryId);
        if (category == null)
            throw new EntityNotFoundException("Category does not exist and therefore it was not possible to delete");

        category.setName(categoryDTOForm.getName());
        category.persist();
        return category;
    }

    @Transactional
    public void deleteCategory(UUID categoryId) {
        Category category = Category.findById(categoryId);
        if (category == null)
            throw new EntityNotFoundException("Category does not exist and therefore it was not possible to delete");

        categoryRepository.deleteCategory(categoryId);
    }
}
