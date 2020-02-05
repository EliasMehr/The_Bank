package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private int accountId;
    private double amount;
    private LocalDate date;

    public Transaction(){

    }

    public Transaction(int transactionId, int accountId, double amount, LocalDate date) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
