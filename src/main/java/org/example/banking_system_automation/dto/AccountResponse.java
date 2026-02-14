package org.example.banking_system_automation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AccountResponse(
        Long accountId,
        String customerName,
        String accountType,
        Double balance,
        String status,
        Double newBalance,
        String transactionStatus,
        String message
) {}
