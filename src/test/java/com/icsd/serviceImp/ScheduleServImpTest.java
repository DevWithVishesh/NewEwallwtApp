package com.icsd.serviceImp;

import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Customer;
import com.icsd.model.mail.MailModule;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleServImpTest {

    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    ScheduleServImp scheduleServImp;


    @Test
    void sendmail() {
        MailModule mailModule = new MailModule();
        mailModule.setSendTo("Raman");
        mailModule.setEmail("raman123@gmail.com");
        mailModule.setExpireDate(LocalDate.now());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        Mockito.verify(javaMailSender, Mockito.times(0)).send(mailMessage);
        String actualEmail = scheduleServImp.sendmail(mailModule);
        assertEquals(mailModule.getEmail(), actualEmail);
        assertThrows(ResourceNotFoundException.class, () -> scheduleServImp.sendmail(null));

    }

    @Test
    void sendWelcomeMail() {
        Customer customer = new Customer();
        customer.setEmailId("s@s.com");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        Mockito.verify(javaMailSender, Mockito.times(0)).send(mailMessage);
        String actualEmail = scheduleServImp.sendWelcomeMail(customer);
        assertEquals(customer.getEmailId(), actualEmail);
        assertThrows(ResourceNotFoundException.class, () -> scheduleServImp.sendWelcomeMail(null));

    }
}