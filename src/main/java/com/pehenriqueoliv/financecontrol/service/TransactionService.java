package com.pehenriqueoliv.financecontrol.service;

import com.pehenriqueoliv.financecontrol.dto.request.TransactionRequest;
import com.pehenriqueoliv.financecontrol.dto.response.BalanceSummaryResponse;
import com.pehenriqueoliv.financecontrol.dto.response.TransactionResponse;
import com.pehenriqueoliv.financecontrol.entity.Category;
import com.pehenriqueoliv.financecontrol.entity.Transaction;
import com.pehenriqueoliv.financecontrol.entity.User;
import com.pehenriqueoliv.financecontrol.entity.TransactionType;
import com.pehenriqueoliv.financecontrol.exception.BusinessException;
import com.pehenriqueoliv.financecontrol.exception.ResourceNotFoundException;
import com.pehenriqueoliv.financecontrol.mapper.TransactionMapper;
import com.pehenriqueoliv.financecontrol.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        User user = userService.findEntityById(request.userId());
        Category category = categoryService.findEntityById(request.categoryId());

        validateCategoryType(request.type(), category);

        Transaction transaction = transactionMapper.toEntity(request, user, category);
        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> findAll(
            Long userId,
            TransactionType type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        userService.findEntityById(userId);

        return transactionRepository
                .findWithFilters(userId, type, categoryId, startDate, endDate, pageable)
                .map(transactionMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TransactionResponse findById(Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        User user = userService.findEntityById(request.userId());
        Category category = categoryService.findEntityById(request.categoryId());

        validateCategoryType(request.type(), category);

        existing.setDescription(request.description());
        existing.setAmount(request.amount());
        existing.setType(request.type());
        existing.setDate(request.date());
        existing.setUser(user);
        existing.setCategory(category);

        return transactionMapper.toResponse(transactionRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        transactionRepository.delete(transaction);
    }

    @Transactional(readOnly = true)
    public BalanceSummaryResponse getBalanceSummary(Long userId) {
        userService.findEntityById(userId);

        BigDecimal totalIncome = transactionRepository
                .sumByUserIdAndType(userId, TransactionType.INCOME);

        BigDecimal totalExpense = transactionRepository
                .sumByUserIdAndType(userId, TransactionType.EXPENSE);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new BalanceSummaryResponse(totalIncome, totalExpense, balance);
    }

    private void validateCategoryType(TransactionType transactionType, Category category) {
        if (!category.getType().equals(transactionType)) {
            throw new BusinessException(
                    "Category type '%s' does not match transaction type '%s'"
                            .formatted(category.getType(), transactionType)
            );
        }
    }
}
