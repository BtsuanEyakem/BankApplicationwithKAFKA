package bank;

import bank.domain.Account;
import bank.domain.AccountEntry;
import bank.domain.Customer;
import bank.repository.AccountJRepository;
import bank.service.AccountService;
import bank.service.BankStatementPrinter;
import bank.service.DTO.AccountDTO.*;
import bank.service.DTO.AccountEntryDTO.AccountEntryDTO;
import bank.service.DTO.CustomerDTO.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collection;


@SpringBootApplication
@EnableKafka
@EnableScheduling
@Async
public class Application implements CommandLineRunner {
	@Autowired
	private AccountService accountService;

	@Autowired
	BankStatementPrinter bankStatementPrinter;

	@Autowired
	AccountJRepository accountJRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {



//		accountService.createAccount(1263862, "Frank Brown");
//		accountService.createAccount(4253892, "John Doe");
//		//System.out.println();
//
//		//use account 1;
//		accountService.deposit(1263862, 240);
//		accountService.deposit(1263862, 529);
//		accountService.withdrawEuros(1263862, 230);
//
//		//use account 2;
//		accountService.deposit(4253892, 12450);
//		accountService.depositEuros(4253892, 200);
//		accountService.transferFunds(4253892, 1263862, 100, "payment of invoice 10232");
//


		bankStatementPrinter.printStatement();


	}
}


