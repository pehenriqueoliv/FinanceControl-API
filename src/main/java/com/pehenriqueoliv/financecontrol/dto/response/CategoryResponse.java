package com.pehenriqueoliv.financecontrol.dto.response;

import com.pehenriqueoliv.financecontrol.entity.TransactionType;

public record CategoryResponse(
        Long id,
        String name,
        TransactionType type
) {}
