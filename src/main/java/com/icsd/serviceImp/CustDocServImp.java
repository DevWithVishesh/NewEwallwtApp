package com.icsd.serviceImp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Customer;
import com.icsd.model.CustomerDocuments;
import com.icsd.repo.Customerdocrepo;
import com.icsd.service.CustDocServ;
import com.icsd.service.CustomerServ;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustDocServImp implements CustDocServ {

    @Autowired
    CustomerServ cs;
    @Autowired
    Customerdocrepo cdrepo;

    @Override
    public int savedoc(Customer customer, String FileType, MultipartFile file) throws IOException {
        log.info("customer exists");
        File serverFile = null;
        System.out.println(file.getOriginalFilename());
        if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                String rootPath = "C:/Users/Lenovo/";
                File dir = new File(rootPath + "/" + "customerdocs" + "/" + customer.getCustomerId());
                if (!dir.exists()) {
                    dir.mkdirs();   
                    log.info("directory created");
                }
                serverFile = new File(dir.getAbsolutePath() + "/" + FileType + ".jpeg");
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                log.info("file created");
                stream.close();
        } else {
            throw new ResourceNotFoundException("Null file not allowed");
        }
        CustomerDocuments cusd = new CustomerDocuments(serverFile.getAbsolutePath(), customer, Date.valueOf(LocalDate.now()),
                FileType);
        customer.setCustomerDocument(cusd);
        CustomerDocuments csdret = cdrepo.save(cusd);
        cs.registerCustomer(customer);
        return csdret.getDocumentuploadid();
    }

    @Override
    public CustomerDocuments fromCustDocId(int customerDocId) {
        Optional<CustomerDocuments> opt = cdrepo.findById(customerDocId);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException("No document on such id");
        }
        return opt.get();
    }


}
