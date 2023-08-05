package com.icsd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icsd.model.Transaction;

public interface Transrepo extends JpaRepository<Transaction, Integer> {

}
