package com.icsd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icsd.dto.TransactionDepositDTO;
import com.icsd.model.Account;
import com.icsd.model.Transaction;
import com.icsd.service.TransServ;

@RestController
@RequestMapping(value = "/Transection")
public class TransController {

    @Autowired
    TransServ tr;

    @PostMapping("/withdraw")
    public Transaction With(@RequestBody TransactionDepositDTO t) {
        return tr.withdrow(t);
    }

    @PostMapping("/deposit")
    public Transaction Dep(@RequestBody TransactionDepositDTO t) {
        return tr.Deposit(t);
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestBody TransactionDepositDTO t) {
        return tr.Transf(t);
    }
}
