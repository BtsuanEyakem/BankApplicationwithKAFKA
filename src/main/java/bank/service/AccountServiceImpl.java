package bank.service;

import bank.domain.Account;
import bank.domain.Accounts;
import bank.domain.Customer;
import bank.integration.jms.JMSSender;
import bank.integration.logging.Logger;
import bank.repository.AccountJpaRepository;
import bank.service.DTO.AccountDTO.*;
import bank.service.DTO.CustomerDTO.CustomerDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private AccountJpaRepository accountRepository;
	@Autowired
	private CurrencyConverter currencyConverter;
	@Autowired
	private JMSSender jmsSender;
	@Autowired
	private Logger logger;

	
	public AccountServiceImpl() {

	}

	public Account createAccount(long accountNumber, String customerName) {
		Account account = new Account(accountNumber);
		Customer customer = new Customer(customerName);
		account.setCustomer(customer);
		publisher.publishEvent(new AccountChangeEvent(customerName, accountNumber));
		accountRepository.saveAccount(account);
		logger.log("createAccount with parameters accountNumber= "+accountNumber+" , customerName= "+customerName);
		return account;
	}

	public void deposit(long accountNumber, double amount) {
		Account account = accountRepository.loadAccount(accountNumber);
		account.deposit(amount);
		accountRepository.updateAccount(account);
		logger.log("deposit with parameters accountNumber= "+accountNumber+" , amount= "+amount);
		publisher.publishEvent(new AccountChangeEvent(accountNumber,amount));
		if (amount > 10000){
			jmsSender.sendJMSMessage("Deposit of $ "+amount+" to account with accountNumber= "+accountNumber);
		}
	}

	public Account getAccount(long accountNumber) {

		Account account = accountRepository.loadAccount(accountNumber);
		return account;
	}

	public Collection<Account> getAllAccounts() {
		return accountRepository.getAccounts();
	}

	public Collection<AccountDTO> getAllAccountDTOs() {
		Collection<AccountDTO> accountDTOs = new ArrayList<>();
		for (Account account : getAllAccounts()) {
			accountDTOs.add(AccountAdaptor.AccountToAccountDTO(account));
		}
		return accountDTOs;
	}

	public AccountDTO getAccountDTO(Account account) {
		return AccountAdaptor.AccountToAccountDTO(account);
	}

	public void withdraw(long accountNumber, double amount) {
		Account account = accountRepository.loadAccount(accountNumber);
		account.withdraw(amount);
		accountRepository.updateAccount(account);
		//publisher.publishEvent(new AccountDTO(accountNumber));
		logger.log("withdraw with parameters accountNumber= "+accountNumber+" , amount= "+amount);
	}

	public void depositEuros(long accountNumber, double amount) {
		Account account = accountRepository.loadAccount(accountNumber);
		double amountDollars = currencyConverter.euroToDollars(amount);
		account.deposit(amountDollars);
		accountRepository.updateAccount(account);
		logger.log("depositEuros with parameters accountNumber= "+accountNumber+" , amount= "+amount);
		if (amountDollars > 10000){
			jmsSender.sendJMSMessage("Deposit of $ "+amount+" to account with accountNumber= "+accountNumber);
		}
	}

	public void withdrawEuros(long accountNumber, double amount) {
		Account account = accountRepository.loadAccount(accountNumber);
		double amountDollars = currencyConverter.euroToDollars(amount);
		account.withdraw(amountDollars);
		accountRepository.updateAccount(account);
		logger.log("withdrawEuros with parameters accountNumber= "+accountNumber+" , amount= "+amount);
	}

	public void transferFunds(long fromAccountNumber, long toAccountNumber, double amount, String description) {
		Account fromAccount = accountRepository.loadAccount(fromAccountNumber);
		Account toAccount = accountRepository.loadAccount(toAccountNumber);
		fromAccount.transferFunds(toAccount, amount, description);
		accountRepository.updateAccount(fromAccount);
		accountRepository.updateAccount(toAccount);
		logger.log("transferFunds with parameters fromAccountNumber= "+fromAccountNumber+" , toAccountNumber= "+toAccountNumber+" , amount= "+amount+" , description= "+description);
		if (amount > 10000){
			jmsSender.sendJMSMessage("TransferFunds of $ "+amount+" from account with accountNumber= "+fromAccount+" to account with accountNumber= "+toAccount);
		}
	}

}
