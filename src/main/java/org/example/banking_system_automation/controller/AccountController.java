package org.example.banking_system_automation.controller;

import org.example.banking_system_automation.dto.AccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AccountResponse(1001L, "Rahul Sharma", "SAVINGS", 5000.0, "ACTIVE", null, null, null)
        );
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                new AccountResponse(accountId, "Rahul Sharma", "SAVINGS", 5000.0, "ACTIVE", null, null, null)
        );
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountResponse> deposit(@PathVariable Long accountId, @RequestBody DepositRequest request) {
        return ResponseEntity.ok(
                new AccountResponse(accountId, null, null, null, null, 7000.0, "SUCCESS", null)
        );
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@PathVariable Long accountId, @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(
                new AccountResponse(accountId, null, null, null, null, 6000.0, "SUCCESS", null)
        );
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<AccountResponse> closeAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                new AccountResponse(null, null, null, null, null, null, null, "Account closed successfully")
        );
    }

    record CreateAccountRequest(String customerName, String accountType, Double initialDeposit) {}
    record DepositRequest(Double amount) {}
    record WithdrawRequest(Double amount) {}
}
