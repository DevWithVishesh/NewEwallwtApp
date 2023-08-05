package com.icsd.scheduler;

import com.icsd.model.Customer;
import com.icsd.model.mail.MailModule;
import com.icsd.service.CustomerServ;
import com.icsd.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MyScheduler {

    @Value("${spring.quartz.startup-delay}")
    int timer;
    @Autowired
    CustomerServ cus;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    Scheduler scheduler;

    @Scheduled(cron = "0 0 0 * * *")
    public void sendMail() {
        List<Customer> lst = cus.getCustomerExpiringTodayOrTomorrow();


        for (Customer c : lst) {

            MailModule mailModule = MailModule.builder().SendTo(c.getFirstName())
                    .Email(c.getEmailId()).ExpireDate(c.getExpireDate()).build();
            log.info("Mail Sent Successfully to " + scheduleService.sendmail(mailModule));
        }
    }

    public void mail(Customer customer) {
        log.info("Mail Sent SuccessFully To " + scheduleService.sendWelcomeMail(customer));
    }


    public Boolean intermediate(Customer customer1) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(EmailJob.class)
                .withIdentity("emailJob-" + customer1.getCustomerId(), "emailGroup")
                .usingJobData("customer", customer1.getEmailId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("emailTrigger-" + customer1.getCustomerId(), "emailGroup")
                .startAt(DateBuilder.futureDate(timer, DateBuilder.IntervalUnit.MINUTE))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        return true;
    }
}
