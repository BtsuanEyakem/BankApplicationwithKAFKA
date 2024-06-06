package bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Accounts {

	private long accountNumber;
	private String name;
	private double amount;
	private String action;



}