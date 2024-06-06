package bank.integration.jms;

import bank.domain.Account;

import bank.domain.Accounts;
import bank.service.AccountService;
import bank.service.AccountServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkeListenerImpl implements KafkaListenerInt {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private AccountService accountService;


    @KafkaListener(topics = {"TopicB"}, groupId = "group_id")
    public void accountListener(@Payload String incomingString) throws JsonProcessingException {

        Accounts incomingAccount = objectMapper.readValue(incomingString, Accounts.class);

        switch (incomingAccount.getAction()){
            case "create":
                accountService.createAccount(incomingAccount.getAccountNumber(), incomingAccount.getName());
                break;
            case "deposit":
                accountService.deposit(incomingAccount.getAccountNumber(), incomingAccount.getAmount());
                break;
            case "withdrow":
                accountService.withdraw(incomingAccount.getAccountNumber(), incomingAccount.getAmount());
                break;
            default:
                System.out.println("Invalid action");
                break;
        }

    }
}
