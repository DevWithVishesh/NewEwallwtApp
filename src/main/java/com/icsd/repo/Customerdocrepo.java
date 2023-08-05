package com.icsd.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icsd.model.CustomerDocuments;

public interface Customerdocrepo extends JpaRepository<CustomerDocuments, Integer> {

}
