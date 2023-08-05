package com.icsd.service;

import java.util.List;

import com.icsd.dto.request.AccountRequestDto;
import com.icsd.model.Account;

public interface AccServ {

	public Account addAccount(AccountRequestDto account);
	public Account saveAccount(Account acc);
	public Account findAccountbyAccountno(int accountNumber);
	public List<Account> getAllAccounts();
	public List<Account> getAllAccountsByCustomerid(int customerid);
	public int deleteAccount(int accountNumber);
}