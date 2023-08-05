package com.icsd.scheduler;

import com.icsd.model.Customer;
import com.icsd.repo.CustomerRepo;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmailJob implements Job {

    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    MyScheduler myScheduler;



    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String emailId = (String) dataMap.get("customer");
        Optional<Customer> customer = customerRepo.findByEmailId(emailId);
        customer.ifPresent(myScheduler::mail);
    }
}