package com.example.wallet.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddAmountRequest {

    @NotNull
    private Long walletId;

    @NotNull
    @Min(1)
    @JsonAlias({"balance"})
    private Double amount;

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}