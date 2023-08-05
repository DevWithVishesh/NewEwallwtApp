package com.icsd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Address 
{
	@Id
	@GeneratedValue
	int addressId;
	String addressLine1;
	String addressLine2;
	String city;
	String state;
	String pincode;
	
	
	
	
	
}
