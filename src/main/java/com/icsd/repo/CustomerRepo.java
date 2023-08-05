package com.icsd.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.icsd.model.Customer;

public interface CustomerRepo  extends JpaRepository<Customer, Integer>{
	
	 Optional<Customer> findByEmailIdAndPassword(String email, String password);

	 Optional<Customer> findByEmailId(String emailId);

	List<Customer> findByExpireDateOrExpireDate(LocalDate l, LocalDate l2);
	Boolean existsByCustomerId(int customerID);
}
