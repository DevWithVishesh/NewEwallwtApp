package com.icsd.serviceImp;

import com.icsd.dto.request.AccountRequestDto;
import com.icsd.exceptionhand.EntityAlreadyExistException;
import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Account;
import com.icsd.model.AccountType;
import com.icsd.model.Customer;
import com.icsd.repo.AccountRepo;
import com.icsd.service.AccServ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountImpTest {
    @Mock
    AccountRepo accountRepo;
    @Mock
    CustomerServiceImp customerServ;
    @InjectMocks
    AccountImp accountImp;

    @Mock
    AccServ mockAccountImp;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addAccount() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        Account account = new Account();
        account.setAccountNumber(1);
        account.setCustomer(customer);
        account.setAccountType(AccountType.SALARY);
        account.setDescription("New account");
        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setCustomerid(1);
        accountRequestDto.setAccountType(AccountType.SALARY);
        when(accountRepo.findByCustomerCustomerIdAndAccountType(account.getCustomer().getCustomerId(), account.getAccountType())).thenReturn(Optional.empty());
        when(customerServ.getCustomerByCustomerId(account.getCustomer().getCustomerId())).thenReturn(customer);
        when(accountRepo.save(new Account())).thenReturn(account);
        Account createdAccount = accountImp.addAccount(accountRequestDto);
        assertEquals(account.getCustomer().getCustomerId(), createdAccount.getCustomer().getCustomerId());
        when(accountRepo.findByCustomerCustomerIdAndAccountType(accountRequestDto.getCustomerid(), accountRequestDto.getAccountType())).thenReturn(Optional.of(account));
        assertThrows(EntityAlreadyExistException.class, () -> accountImp.addAccount(accountRequestDto));

    }

    @Test
    void findAccountByAccountNo() {
        int accountNumber = 1;
        Account account = new Account();
        account.setAccountType(AccountType.SALARY);
        Account actualAccount = new Account();
        actualAccount.setAccountType(AccountType.SALARY);
        when(accountRepo.findById(accountNumber)).thenReturn(Optional.of(account));
        accountImp.findAccountbyAccountno(accountNumber);
        when(accountRepo.findById(accountNumber)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountImp.findAccountbyAccountno(accountNumber));
    }

    @Test
    void getAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        Account account = new Account();
        account.setAccountNumber(1);
        accountList.add(account);
        when(accountRepo.findAll()).thenReturn(accountList);
        List<Account> accounts = accountImp.getAllAccounts();
        assertEquals(account.getAccountNumber(), accounts.get(0).getAccountNumber());
    }

    @Test
    void getAllAccountsByCustomerId() {
        List<Account> accountList = new ArrayList<>();
        int customerID = 1;
        Account account = new Account();
        account.setAccountNumber(1);
        accountList.add(account);
        when(accountRepo.findByCustomerCustomerId(customerID)).thenReturn(accountList);
        List<Account> accounts = accountImp.getAllAccountsByCustomerid(customerID);
        assertEquals(account.getAccountNumber(), accounts.get(0).getAccountNumber());
        List<Account> emptyAccountList = new ArrayList<>();
        when(accountRepo.findByCustomerCustomerId(customerID)).thenReturn(emptyAccountList);
        assertThrows(ResourceNotFoundException.class, () -> accountImp.getAllAccountsByCustomerid(customerID));
    }

    @Test
    void deleteAccount() {
        int accountNumber = 1;
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        when(mockAccountImp.findAccountbyAccountno(accountNumber)).thenReturn(account);
        verify(accountRepo,times(0)).deleteById(accountNumber);
        int returnedAccountNo = accountImp.deleteAccount(accountNumber);
        assertEquals(accountNumber, returnedAccountNo);
    }

    @Test
    void saveAccount() {
        Account account = new Account();
        account.setAccountNumber(1);
        when(accountRepo.save(account)).thenReturn(account);
        Account acctualAccount=accountImp.saveAccount(account);
        assertEquals(account,acctualAccount);
    }
}