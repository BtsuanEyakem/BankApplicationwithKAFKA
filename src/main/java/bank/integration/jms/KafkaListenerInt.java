package bank.integration.jms;

import bank.domain.Account;
import bank.domain.Accounts;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.messaging.handler.annotation.Payload;

public interface KafkaListenerInt {

    public void accountListener(String incomingAccount) throws JsonProcessingException;

}
