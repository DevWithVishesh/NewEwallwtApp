package com.icsd.serviceImp;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.icsd.dto.request.AccountRequestDto;
import com.icsd.exceptionhand.EntityAlreadyExistException;
import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Account;
import com.icsd.model.Customer;
import com.icsd.repo.AccountRepo;
import com.icsd.service.AccServ;
import com.icsd.service.CustomerServ;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountImp implements AccServ {

    @Autowired
    AccountRepo accountRepo;
    @Autowired
    CustomerServ customerServ;
    @Autowired
    AccServ accServ;

    @Override
    public Account addAccount(AccountRequestDto account) {
        log.info("Inside AddAccount");

        Optional<Account> ac = accountRepo.findByCustomerCustomerIdAndAccountType(account.getCustomerid(), account.getAccountType());
        if (ac.isPresent()) {
            throw new EntityAlreadyExistException("Your Account of such type is already exist");
        }
        Customer customer = customerServ.getCustomerByCustomerId(account.getCustomerid());
        Account acouAccount = Account.builder().
                accountType(account.getAccountType())
                .customer(customer)
                .description(account.getDescription())
                .openingBalance(account.getOpeningBalance())
                .openingDate(account.getOpeningDate()).build();
        log.info("Account Build Successfully" + acouAccount);
        accountRepo.save(acouAccount);
        log.info("Account Saved SuccessFully");
        return acouAccount;
    }

    @Override
    public Account findAccountbyAccountno(int accountNumber) {
        log.info("Inside Account by AccountNo");
        Optional<Account> acc = accountRepo.findById(accountNumber);
        if (acc.isEmpty()) {
            throw new ResourceNotFoundException("You Don't Have any Account of such Account number");
        }
        log.info("account found Successfully");
        return acc.get();
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    @Override
    public List<Account> getAllAccountsByCustomerid(int customer) {
        List<Account> accountList = accountRepo.findByCustomerCustomerId(customer);
        if (accountList.isEmpty()) {
            throw new ResourceNotFoundException("You Don't Have any Account Yet create one");
        }
        return accountList;
    }

    @Override
    public int deleteAccount(int accountNumber) {
        Account ac = accServ.findAccountbyAccountno(accountNumber);
        accountRepo.deleteById(ac.getAccountNumber());
        log.info("Account Deleted Successfully");
        return accountNumber;
    }

    @Override
    public Account saveAccount(Account acc) {
        return accountRepo.save(acc);
    }


}
