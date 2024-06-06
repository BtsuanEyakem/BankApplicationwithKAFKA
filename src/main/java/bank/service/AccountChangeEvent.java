package bank.service;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AccountChangeEvent {
    private long accountNumber;
    private double amount;
    private String customerName;
    public AccountChangeEvent() {

    }

    public AccountChangeEvent(long accountNumber, double amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public AccountChangeEvent(String customerName, long accountNumber) {
        this.customerName = customerName;
        this.accountNumber = accountNumber;
    }

    public AccountChangeEvent(long accountNumber, double amount, String customerName) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.customerName = customerName;
    }
}
