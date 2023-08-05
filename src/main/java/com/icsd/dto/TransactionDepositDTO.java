package com.icsd.dto;

//txtCustid: 1
//selAccountNo: 1
//txtAmount: 10000
//txtDesc: desc
//selfromAcc: 1
public class TransactionDepositDTO {

	int accountNumber;
	double amount;
	String description;
	int fromAccountNumber;
	
	
	
	public TransactionDepositDTO() {
		super();
	}
	public TransactionDepositDTO( int accountNumber, double amount, String description,
			int fromAccountNumber) {
		super();
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.description = description;
		this.fromAccountNumber = fromAccountNumber;
	}
	@Override
	public String toString() {
		return "TransactionDepositDTO [ accountNumber=" + accountNumber + ", amount="
				+ amount + ", description=" + description + ", fromAccountNumber=" + fromAccountNumber + "]";
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getFromAccountNumber() {
		return fromAccountNumber;
	}
	public void setFromAccountNumber(int fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}
	
	
}
