package com.icsd.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction 
{
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE)
	private int transactionId;
	
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	
	private LocalDate transactionDate;
	private double amount;
	private String description;
	
	@ManyToOne
	@JoinColumn(name="frmAccFk")
	private Account fromAccount;
	
	@ManyToOne
	@JoinColumn(name="toAccFk")
	private Account toAccount;

	public Transaction(TransactionType transactionType, LocalDate transactionDate, double amount, String description,
			Account fromAccount, Account toAccount) {
		super();
		this.transactionType = transactionType;
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.description = description;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}
	
	
}
