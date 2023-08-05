package com.icsd.model;

import java.sql.Date;
import java.time.LocalDate;

import com.icsd.dto.request.AccountRequestDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

	@Id
	@GeneratedValue
	private int accountNumber;
	@ManyToOne
	@JoinColumn(name="customerFk")
	private Customer customer;
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	private double openingBalance;
	private Date   openingDate;
	private String description;
	
	
	
	
	
}
