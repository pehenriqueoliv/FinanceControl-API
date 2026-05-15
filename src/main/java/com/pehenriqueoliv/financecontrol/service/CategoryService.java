package com.pehenriqueoliv.financecontrol.service;

import com.pehenriqueoliv.financecontrol.dto.request.CategoryRequest;
import com.pehenriqueoliv.financecontrol.dto.response.CategoryResponse;
import com.pehenriqueoliv.financecontrol.entity.Category;
import com.pehenriqueoliv.financecontrol.entity.TransactionType;
import com.pehenriqueoliv.financecontrol.exception.BusinessException;
import com.pehenriqueoliv.financecontrol.exception.ResourceNotFoundException;
import com.pehenriqueoliv.financecontrol.mapper.CategoryMapper;
import com.pehenriqueoliv.financecontrol.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new BusinessException("Category already exists: " + request.name());
        }

        Category category = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findByType(TransactionType type) {
        return categoryRepository.findByType(type)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public void delete(Long id) {
        Category category = findEntityById(id);
        categoryRepository.delete(category);
    }
}
