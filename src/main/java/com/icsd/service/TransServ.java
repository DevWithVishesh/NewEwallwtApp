package com.icsd.service;


import com.icsd.dto.TransactionDepositDTO;
import com.icsd.model.Transaction;

public interface TransServ {

    Transaction SaveTrans(Transaction tr);

    Transaction withdrow(TransactionDepositDTO dt);

    Transaction Deposit(TransactionDepositDTO dt);

    Transaction Transf(TransactionDepositDTO dt);


}
