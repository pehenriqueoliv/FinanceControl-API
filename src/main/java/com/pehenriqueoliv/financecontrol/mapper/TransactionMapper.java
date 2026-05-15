package com.pehenriqueoliv.financecontrol.mapper;

import com.pehenriqueoliv.financecontrol.dto.request.TransactionRequest;
import com.pehenriqueoliv.financecontrol.dto.response.TransactionResponse;
import com.pehenriqueoliv.financecontrol.entity.Category;
import com.pehenriqueoliv.financecontrol.entity.Transaction;
import com.pehenriqueoliv.financecontrol.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private final CategoryMapper categoryMapper;

    public TransactionMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public Transaction toEntity(TransactionRequest request, User user, Category category) {
        return Transaction.builder()
                .description(request.description())
                .amount(request.amount())
                .type(request.type())
                .date(request.date())
                .user(user)
                .category(category)
                .build();
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDate(),
                categoryMapper.toResponse(transaction.getCategory()),
                transaction.getUser().getId(),
                transaction.getCreatedAt()
        );
    }
}
