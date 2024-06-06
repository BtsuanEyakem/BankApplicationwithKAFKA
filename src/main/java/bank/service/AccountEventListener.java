package bank.service;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AccountEventListener {

    @Async
    @EventListener
    public void onAccountCreated(AccountChangeEvent event) {
        System.out.println("Account created with Account ########################################################: " + event.getAccountNumber()+ " and Name: "+event.getCustomerName());
    }
    @Async
    @EventListener
    public void deposit(AccountChangeEvent event){
        System.out.println("Account deposited with amount ########################################################: " + event.getAmount()+ " and AccountNumber: "+event.getAccountNumber());
    }
}
