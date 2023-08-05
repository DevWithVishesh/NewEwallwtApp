package com.icsd.repo;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icsd.model.Account;
import com.icsd.model.AccountType;

public interface AccountRepo extends JpaRepository<Account, Integer> {

	List<Account> findByCustomerCustomerId(int custid);
	 Optional<Account> findByCustomerCustomerIdAndAccountType(int custid,AccountType actype);
	
}
