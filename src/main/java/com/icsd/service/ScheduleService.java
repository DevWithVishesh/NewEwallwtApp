package com.icsd.service;

import com.icsd.model.Customer;
import com.icsd.model.mail.MailModule;

public interface ScheduleService {

    String sendmail(MailModule mailModule);

    String sendWelcomeMail(Customer customer);

}
