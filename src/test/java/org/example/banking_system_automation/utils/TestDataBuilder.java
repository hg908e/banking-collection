package org.example.banking_system_automation.utils;

import org.example.banking_system_automation.models.AccountRequest;
import org.example.banking_system_automation.models.DepositRequest;
import org.example.banking_system_automation.models.WithdrawRequest;

public final class TestDataBuilder {

    private TestDataBuilder() {}

    public static AccountRequest defaultAccountRequest() {
        return AccountRequest.builder()
                .customerName("Rahul Sharma")
                .accountType("SAVINGS")
                .initialDeposit(5000.0)
                .build();
    }

    public static DepositRequest defaultDepositRequest() {
        return DepositRequest.builder()
                .amount(2000.0)
                .build();
    }

    public static WithdrawRequest defaultWithdrawRequest() {
        return WithdrawRequest.builder()
                .amount(1000.0)
                .build();
    }
}
