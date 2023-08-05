package com.icsd.paging;

import com.icsd.dto.request.CustomerPageRequestDto;
import com.icsd.model.Customer;
import org.springframework.data.domain.Page;


public interface CustomerPageServ {
Page<Customer> getCustomerPages(CustomerPageRequestDto customerPageRequestDto,int pageNo);

}
