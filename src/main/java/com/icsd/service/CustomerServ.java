package com.icsd.service;
import com.icsd.dto.request.CustomerRequestDto;
import com.icsd.model.Customer;
import com.lowagie.text.Document;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.quartz.SchedulerException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface CustomerServ {
    Customer registerCustomer(Customer customer);

    Customer getCustomerByEmailAndPassword(String email, String password);

    Customer createCustomer(@Valid CustomerRequestDto customerRequest) throws SchedulerException;

    Boolean isCustomerExistsByID(int customerID);

    Customer getCustomerByEmailId(String strEmailId);

    Customer getCustomerByCustomerId(int strCustomerId);

    List<Customer> getCustomerExpiringTodayOrTomorrow();

    InputStreamResource CreateSampleSheet() throws IOException;


    ArrayList<String> sendBulkData(MultipartFile multipartFile) throws IOException, InvalidFormatException, SchedulerException;

    Document CreateCustomerPDF(int customerId, HttpServletResponse response) throws IOException;

    ArrayList<String> sheetCalc(XSSFSheet sheet) throws SchedulerException;
}
