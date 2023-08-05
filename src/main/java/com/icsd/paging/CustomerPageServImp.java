package com.icsd.paging;

import com.icsd.dto.request.CustomerPageRequestDto;
import com.icsd.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomerPageServImp implements CustomerPageServ {
    @Autowired
    CustomerPageRepo customerPageRepo;

    /*
     * Get the CustomerPageRequestDto and page number and return the page
     * */
    @Override
    public Page<Customer> getCustomerPages(CustomerPageRequestDto customerPageRequestDto, int pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "registrationDate");
        if (Objects.equals(customerPageRequestDto.getSortDirection(), "asc")) {
            sort = Sort.by(Sort.Direction.ASC, "registrationDate");
        }

        if (customerPageRequestDto.getSortField() != null) {
            sort = Sort.by(Sort.Direction.DESC, customerPageRequestDto.getSortField());
            if (Objects.equals(customerPageRequestDto.getSortDirection(), "asc")) {
                sort = Sort.by(Sort.Direction.ASC, customerPageRequestDto.getSortField());
            }
        }
        Pageable pageable = PageRequest.of(pageNo, customerPageRequestDto.getPageLimit(), sort);
        return customerPageRepo.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailIdContainingIgnoreCase(customerPageRequestDto.getSearch(), customerPageRequestDto.getSearch(), customerPageRequestDto.getSearch(), pageable);
    }
}
