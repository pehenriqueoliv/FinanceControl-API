package com.pehenriqueoliv.financecontrol.repository;

import com.pehenriqueoliv.financecontrol.entity.Category;
import com.pehenriqueoliv.financecontrol.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByType(TransactionType type);
    boolean existsByName(String name);
}
