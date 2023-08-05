package com.icsd.paging;

import com.icsd.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerPageRepo  extends PagingAndSortingRepository<Customer,Integer> {
    Page<Customer> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailIdContainingIgnoreCase(String firstName, String lastName, String emailId, Pageable pageable );

}
