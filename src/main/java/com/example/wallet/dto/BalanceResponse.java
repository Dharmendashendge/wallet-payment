package com.example.wallet.dto;

public class BalanceResponse {

    private Long userId;
    private double balance;
    private String currency;

    public BalanceResponse() {
    }

    public BalanceResponse(Long userId, double balance, String currency) {
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}