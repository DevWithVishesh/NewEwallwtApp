package com.icsd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icsd.model.Account;
import com.icsd.model.Address;

public interface Addressrepo  extends JpaRepository<Address, Integer>{

	
}
