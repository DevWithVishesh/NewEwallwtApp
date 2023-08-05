package com.icsd.dto.request;



import java.sql.Date;

import com.icsd.model.AccountType;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountRequestDto {

	@NotBlank(message = "custid name should not be blank")
	 @NotNull(message="custid name should not be null")
	private int customerid;	
	
	@NotBlank(message = "AccountType name should not be blank")
	 @NotNull(message="AccountType name should not be null")
	private AccountType accountType;
	
	@NotBlank(message = "openingbalane name should not be blank")
	 @NotNull(message="openingbalance name should not be null")
	private double openingBalance;
	
	@NotBlank(message = "openingDate name should not be blank")
	 @NotNull(message="openingdate name should not be null")
	private Date   openingDate;

	
	@NotBlank(message = "Description name should not be blank")
	 @NotNull(message="Desccription name should not be null")
	private String description;
	
	
	
}
