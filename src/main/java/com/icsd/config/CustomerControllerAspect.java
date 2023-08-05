package com.icsd.config;


import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.service.CustomerServ;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomerControllerAspect {
    @Autowired
    CustomerServ customerServ;

    /**
     * Check Customer Exists with customerID Before API call
     * @param customerId: int
     */

    @Before("execution(* com.icsd.controller.CustomerController.getCustomerByCustomerId(..)) && args(customerId) )")
    public void checkID(int customerId){
        System.out.println("hello");
        if(!customerServ.isCustomerExistsByID(customerId)){
           throw new ResourceNotFoundException("No Customer Found");
       }
    }

    @AfterReturning("execution(* com.icsd.controller.CustomerController.getCustomerByCustomerId(..)))")
    public void afterReturn(){
System.out.println("Thanks for Creating the Account");
    }





}
