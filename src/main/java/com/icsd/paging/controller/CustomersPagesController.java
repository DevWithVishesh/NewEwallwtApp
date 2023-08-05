package com.icsd.paging.controller;

import com.icsd.dto.request.CustomerPageRequestDto;
import com.icsd.model.Customer;
import com.icsd.paging.CustomerPageServ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/page")
public class CustomersPagesController {
    @Autowired
    CustomerPageServ customerPageServ;

/*
* controller for get Customer pages
* */
    @GetMapping("/getAll/{pageNo}")
    public ResponseEntity<Page<Customer>> getCustomerByPagination(@RequestBody CustomerPageRequestDto customerPageRequestDto, @PathVariable int pageNo) {
        return ResponseEntity.ok(customerPageServ.getCustomerPages(customerPageRequestDto,pageNo));
    }

}
