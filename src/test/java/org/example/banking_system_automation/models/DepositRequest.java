package org.example.banking_system_automation.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositRequest {
    private Double amount;

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }
}
