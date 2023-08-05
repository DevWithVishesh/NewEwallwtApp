package com.icsd.serviceImp;

import com.icsd.dto.TransactionDepositDTO;
import com.icsd.exceptionhand.IcsdException;
import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Account;
import com.icsd.model.AccountType;
import com.icsd.model.Transaction;
import com.icsd.model.TransactionType;
import com.icsd.repo.Transrepo;
import com.icsd.service.AccServ;
import com.icsd.service.TransServ;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransImpTest {

    @InjectMocks
    TransImp transImp;
    @Mock
    Transrepo transrepo;
    @Mock
    AccServ acs;

    @Mock
    TransServ transServ;


    @Test
    void saveTrans() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        Mockito.when(transrepo.save(transaction)).thenReturn(transaction);
        Transaction actualTransaction = transImp.SaveTrans(transaction);
        assertEquals(transaction, actualTransaction);
    }

    @Test
    void withdraw() {
        TransactionDepositDTO transactionDepositDTO = new TransactionDepositDTO();
        transactionDepositDTO.setAccountNumber(1);
        transactionDepositDTO.setFromAccountNumber(2);
        transactionDepositDTO.setAmount(600);
        Mockito.when(acs.findAccountbyAccountno(transactionDepositDTO.getAccountNumber())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> transImp.withdrow(transactionDepositDTO));
        Account account = new Account();
        account.setAccountNumber(2);
        account.setOpeningBalance(200);
        account.setAccountType(AccountType.SALARY);
        Mockito.when(acs.findAccountbyAccountno(transactionDepositDTO.getAccountNumber())).thenReturn(account);
        Mockito.when(acs.findAccountbyAccountno(transactionDepositDTO.getFromAccountNumber())).thenReturn(account);
        assertThrows(IcsdException.class, () -> transImp.withdrow(transactionDepositDTO));
        account.setOpeningBalance(5000);
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setFromAccount(account);
        Mockito.when(transServ.SaveTrans(Mockito.any(Transaction.class))).thenReturn(transaction);
        Mockito.when(acs.saveAccount(account)).thenReturn(account);
        Transaction actualTransaction = transImp.withdrow(transactionDepositDTO);
        assertEquals(transaction.getFromAccount(), actualTransaction.getFromAccount());
    }

    @Test
    void deposit() {
        TransactionDepositDTO transactionDepositDTO = new TransactionDepositDTO();
        transactionDepositDTO.setAccountNumber(1);
        transactionDepositDTO.setFromAccountNumber(2);
        transactionDepositDTO.setAmount(600);
        Mockito.when(acs.findAccountbyAccountno(transactionDepositDTO.getAccountNumber())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> transImp.Deposit(transactionDepositDTO));
        Account account = new Account();
        account.setAccountNumber(1);
        account.setOpeningBalance(200);
        account.setAccountType(AccountType.SALARY);
        Mockito.when(acs.findAccountbyAccountno(transactionDepositDTO.getAccountNumber())).thenReturn(account);
        Mockito.when(acs.findAccountbyAccountno(transactionDepositDTO.getFromAccountNumber())).thenReturn(account);
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setFromAccount(account);
        account.setOpeningBalance(5000);
        Mockito.when(transServ.SaveTrans(Mockito.any(Transaction.class))).thenReturn(transaction);
        Mockito.when(acs.saveAccount(account)).thenReturn(account);
        Transaction actualTransaction = transImp.Deposit(transactionDepositDTO);
        assertEquals(transaction.getFromAccount(), actualTransaction.getFromAccount());
    }

    @Test
    void trans() {
        TransactionDepositDTO transactionDepositDTO = new TransactionDepositDTO();
        transactionDepositDTO.setAccountNumber(1);
        transactionDepositDTO.setFromAccountNumber(2);
        transactionDepositDTO.setAmount(600);
        Mockito.when(acs.findAccountbyAccountno(transactionDepositDTO.getAccountNumber())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> transImp.Transf(transactionDepositDTO));
        Account account = new Account();
        account.setAccountNumber(2);
        account.setOpeningBalance(200);
        account.setAccountType(AccountType.SALARY);
        Mockito.when(acs.findAccountbyAccountno(transactionDepositDTO.getAccountNumber())).thenReturn(account);
        Mockito.when(acs.findAccountbyAccountno(transactionDepositDTO.getFromAccountNumber())).thenReturn(account);
        assertThrows(IcsdException.class, () -> transImp.Transf(transactionDepositDTO));
        account.setOpeningBalance(5000);
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setFromAccount(account);
        Mockito.when(transServ.SaveTrans(Mockito.any(Transaction.class))).thenReturn(transaction);
        Mockito.when(acs.saveAccount(account)).thenReturn(account);
        Transaction actualTransaction = transImp.Transf(transactionDepositDTO);
        assertEquals(transaction.getFromAccount(), actualTransaction.getFromAccount());

    }
}