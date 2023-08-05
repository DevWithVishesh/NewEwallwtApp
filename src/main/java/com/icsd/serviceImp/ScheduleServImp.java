package com.icsd.serviceImp;

import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Customer;
import com.icsd.model.mail.MailModule;
import com.icsd.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServImp implements ScheduleService {

    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendmail(MailModule mailModule) {
        try {
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(mailModule.getEmail());
            mailMessage.setText("Hi" + mailModule.getSendTo() +
                    "Your EWallet Subscription is getting Expired on");
            mailMessage.setSubject("Opps! your Subscription is getting expired");
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Email Failed to Sent");
        }
        return mailModule.getEmail();
    }


    @Override
    public String sendWelcomeMail(Customer customer) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(customer.getEmailId());
            mailMessage.setText("Hi" + customer.getFirstName() +
                    "Welcome to Ewallet Application");
            mailMessage.setSubject("Welcome");
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Email Failed to Sent");
        }
        return customer.getEmailId();
    }


}