package org.example.banking_system_automation.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRequest {
    private String customerName;
    private String accountType;
    private Double initialDeposit;

    @JsonProperty("customerName")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("accountType")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("initialDeposit")
    public Double getInitialDeposit() {
        return initialDeposit;
    }
}
