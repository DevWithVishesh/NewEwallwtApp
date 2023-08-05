package com.icsd.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icsd.dto.request.AccountRequestDto;
import com.icsd.model.Account;
import com.icsd.service.AccServ;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/Account")
@CrossOrigin(value = "*")
@Slf4j
public class AccController {
	@Autowired
	AccServ acr;

	/**
	 *
	 * @param acc
	 * @return
	 */
	@PostMapping("/saveAcc")
	public Account saveacc(@RequestBody AccountRequestDto acc)
	{
		return acr.addAccount(acc);
	}

	/**
	 *
	 * @return list of accounts
	 */
	@GetMapping("/getallacc")
	public List<Account> getallacc(){
		return 	acr.getAllAccounts();
	}
	
    @GetMapping("/allaccbycustid/{id}")
    public List<Account> gettransbyaccno(@PathVariable int id)
    {
    	return acr.getAllAccountsByCustomerid(id);
	}
	
    @GetMapping("/dltcustbycustid/{id}")
    public int removeit(@PathVariable int id)
    {	
    	 acr.deleteAccount(id);
    	 return id;
	}


	
}
