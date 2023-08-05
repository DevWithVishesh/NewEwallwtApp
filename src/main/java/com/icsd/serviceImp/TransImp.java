package com.icsd.serviceImp;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icsd.dto.TransactionDepositDTO;
import com.icsd.exceptionhand.IcsdException;
import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Account;
import com.icsd.model.Transaction;
import com.icsd.model.TransactionType;
import com.icsd.repo.Transrepo;
import com.icsd.service.AccServ;
import com.icsd.service.TransServ;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransImp implements TransServ {

    @Autowired
    Transrepo transrepo;
    @Autowired
    AccServ acs;
    @Autowired
    TransServ transServ;

    @Override
    public Transaction SaveTrans(Transaction tr) {
        log.info("Inside save Transaction ");
        return transrepo.save(tr);
    }


    @Override
    public Transaction withdrow(TransactionDepositDTO dt) {
        Account ac = acs.findAccountbyAccountno(dt.getAccountNumber());
        Account ac2 = acs.findAccountbyAccountno(dt.getFromAccountNumber());
        if (ac == null || ac2==null) {
            throw new ResourceNotFoundException("Account not found");
        }
        if (ac.getOpeningBalance() < dt.getAmount()) {
            throw new IcsdException("Your Account Don't Have Such Amount in your balance");
        }
        ac.setOpeningBalance(ac.getOpeningBalance() - dt.getAmount());
        Transaction t = new Transaction(TransactionType.DEBIT, LocalDate.now(), dt.getAmount(), dt.getDescription(), ac2, ac);
        log.info("Amount Credited Successfully");
        log.info("Transaction Done with ID" + transServ.SaveTrans(t).getTransactionId());
        acs.saveAccount(ac);
        log.info("Account Balance Updated Successfully");
        return t;
    }

    @Override
    public Transaction Deposit(TransactionDepositDTO dt) {
        Account ac = acs.findAccountbyAccountno(dt.getAccountNumber());
        Account ac2 = acs.findAccountbyAccountno(dt.getFromAccountNumber());
        if (ac == null || ac2 ==null) {
            throw new ResourceNotFoundException("Account not found");
        }
        ac.setOpeningBalance(ac.getOpeningBalance() + dt.getAmount());
        Transaction t = new Transaction(TransactionType.CREDIT, LocalDate.now(), dt.getAmount(), dt.getDescription(), ac2, ac);
        log.info("Amount Credited Successfully");
        log.info("Transaction Done with ID" + transServ.SaveTrans(t).getTransactionId());
        acs.saveAccount(ac);
        log.info("Acount Balance Updated Successfully");

        return t;
    }

    @Override
    public Transaction Transf(TransactionDepositDTO dt) {

        Account ac = acs.findAccountbyAccountno(dt.getAccountNumber());
        Account ac2 = acs.findAccountbyAccountno(dt.getFromAccountNumber());
        if (ac == null || ac2==null) {
            throw new ResourceNotFoundException("Account not found");
        }  else if (ac2.getOpeningBalance() < dt.getAmount()) {
            throw new IcsdException("Your Account Don't Have Such Amount in your balance");
        }

        ac.setOpeningBalance(ac.getOpeningBalance() + dt.getAmount());
        ac2.setOpeningBalance(ac2.getOpeningBalance() - dt.getAmount());
        Transaction t1 = new Transaction(TransactionType.DEBIT, LocalDate.now(), dt.getAmount(), dt.getDescription(), ac2, ac);
        log.info("Transaction Done with ID" + transServ.SaveTrans(t1).getTransactionId());
        log.info("Amount Debited Successfully");

        Transaction t = new Transaction(TransactionType.CREDIT, LocalDate.now(), dt.getAmount(), dt.getDescription(), ac2, ac);
        log.info("Transaction Done with ID" + transServ.SaveTrans(t).getTransactionId());
        log.info("Amount Credited Successfully");

        acs.saveAccount(ac);
        log.info("Acount1 Balance Updated Successfully");
        acs.saveAccount(ac2);
        log.info("Acount2 Balance Updated Successfully");


        return t;
    }


}
