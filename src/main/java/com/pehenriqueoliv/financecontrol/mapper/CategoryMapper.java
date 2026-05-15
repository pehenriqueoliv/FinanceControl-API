package com.pehenriqueoliv.financecontrol.mapper;

import com.pehenriqueoliv.financecontrol.dto.request.CategoryRequest;
import com.pehenriqueoliv.financecontrol.dto.response.CategoryResponse;
import com.pehenriqueoliv.financecontrol.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.name())
                .type(request.type())
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType()
        );
    }
}
