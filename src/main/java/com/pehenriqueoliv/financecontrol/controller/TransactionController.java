package com.pehenriqueoliv.financecontrol.controller;

import com.pehenriqueoliv.financecontrol.dto.request.TransactionRequest;
import com.pehenriqueoliv.financecontrol.dto.response.BalanceSummaryResponse;
import com.pehenriqueoliv.financecontrol.dto.response.TransactionResponse;
import com.pehenriqueoliv.financecontrol.entity.TransactionType;
import com.pehenriqueoliv.financecontrol.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> findAll(
            @RequestParam Long userId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "date") Pageable pageable
    ) {
        return ResponseEntity.ok(
                transactionService.findAll(userId, type, categoryId, startDate, endDate, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid TransactionRequest request
    ) {
        return ResponseEntity.ok(transactionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<BalanceSummaryResponse> getSummary(@RequestParam Long userId) {
        return ResponseEntity.ok(transactionService.getBalanceSummary(userId));
    }
}
