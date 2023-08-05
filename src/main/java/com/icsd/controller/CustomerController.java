package com.icsd.controller;

import com.icsd.exceptionhand.IcsdException;
import com.icsd.model.Address;
import com.icsd.serviceImp.AuthService;
import com.lowagie.text.Document;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.icsd.apiresponce.ApiResponse;
import com.icsd.dto.CustomerLoginDTO;
import com.icsd.dto.request.CustomerRequestDto;
import com.icsd.model.Customer;
import com.icsd.service.CustomerServ;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping(value = "/customer")
@Slf4j
public class CustomerController {

    @Autowired
    CustomerServ cs;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AuthService authService;

    /**
     * Create a new Customer with CustomerRequestDto
     * And Also send Welcome Mail to customer after registration
     *
     * @param customerRequest:CustomerRequestDto
     * @return ResponseEntity
     * @throws SchedulerException if n
     */
    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponse> createCustomer(@RequestBody @Valid CustomerRequestDto customerRequest) throws SchedulerException {
        log.info("inside create method of customer controller");
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Customer Created Successfully", cs.createCustomer(customerRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    /**
     * find Customer By EmailID
     *
     * @param emailId:String
     * @return ResponseEntity
     */
    @GetMapping("/findByEmail/{emailId}")
    public ResponseEntity<Customer> getCustomerByEmailId(@PathVariable String emailId) {

        Customer customer = cs.getCustomerByEmailId(emailId);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }

    }

    /**
     * find Customer By Customer ID
     *
     * @param customerId:int
     * @return ResponseEntity
     */

    @GetMapping("/findById/{customerId}")
    public ResponseEntity<Customer> getCustomerByCustomerId(@PathVariable int customerId) {
        log.info("In Customer Controller");

        Customer customer = cs.getCustomerByCustomerId(customerId);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody CustomerLoginDTO customerLoginDTO) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Customer login Successfully", authService.authenticate(customerLoginDTO));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    /**
     * Find Customer By EmailID and Password
     * Help to Log In to the Application
     *
     * @param customerLoginDTO:CustomerLoginDTO
     * @return ResponseEntity
     */
    @PostMapping("/getCustomerByEmailAndPwd")
    public ResponseEntity<ApiResponse> getCustomerByEmailAndPwd(@RequestBody CustomerLoginDTO customerLoginDTO) {
        Customer customer = cs.getCustomerByEmailAndPassword(customerLoginDTO.getEmailId(), customerLoginDTO.getPwd());
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Customer login Successfully", customer);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }

    /**
     * Create A sample for creating an Excel Sample For Customer Registration
     *
     * @return ResponseEntity
     */
    @GetMapping("/createExcelSample")
    public ResponseEntity<InputStreamResource> createExcelSample() {
        try {
            String filename = "CustomerSampleSheet.xlsx";
            InputStreamResource file = cs.CreateSampleSheet();
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename).contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
        } catch (IOException e) {
            throw new IcsdException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    /**
     * Create Customers from Excel
     *
     * @param file:MultipartFile
     * @return ResponseEntity Messages for data saved
     * @throws IOException            when no file found
     * @throws InvalidFormatException when file format is Invalid
     * @throws SchedulerException     send mail after Registration
     */
    @PostMapping("/saveBulkDataFromExcel")
    public ResponseEntity<ArrayList<String>> saveBulkDataFromExcel(@Validated @RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException, SchedulerException {
        return new ResponseEntity<>(cs.sendBulkData(file), HttpStatus.OK);
    }

    /**
     * For getting A PDF sample for your Account with All details with Transaction
     *
     * @param customerId:int
     * @param response:
     * @return ResponseEntity with File
     * @throws IOException when File is Unable to gets Created
     */
    @GetMapping("/pdf/{customerId}")
    public ResponseEntity<Document> customerPDFDownload(@PathVariable int customerId, HttpServletResponse response) throws IOException {
        Document file = cs.CreateCustomerPDF(customerId, response);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        return ResponseEntity.ok().header(headerKey, headerValue)
                .body(file);
    }

    /**
     * Getting Address using RestTemplate
     *
     * @param addressId:int
     * @return : Account
     */
    @GetMapping("/getAddress/{addressId}")
    public ResponseEntity<Address> addressResponseEntity(@PathVariable int addressId) {
        return restTemplate
                .exchange("http://localhost:8080/Address/getById/{addressId}", HttpMethod.GET, null, Address.class, addressId);
    }


}