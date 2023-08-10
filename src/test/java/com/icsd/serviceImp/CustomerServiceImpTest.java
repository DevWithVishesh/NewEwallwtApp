package com.icsd.serviceImp;

import com.icsd.dto.request.CustomerRequestDto;
import com.icsd.exceptionhand.EntityAlreadyExistException;
import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Address;
import com.icsd.model.Customer;
import com.icsd.repo.Addressrepo;
import com.icsd.repo.CustomerRepo;
import com.icsd.scheduler.MyScheduler;
import com.icsd.service.AddServ;
import com.icsd.service.CustomerServ;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.digester.ArrayStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
class CustomerServiceImpTest {
    @Mock
    MultipartFile file;

    @Mock
    CustomerRepo customerRepo;


    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    CustomerServ customerServ;

    @Mock
    AddServ addServ;

    @InjectMocks
    CustomerServiceImp customerServiceImp;

    @Mock
    MyScheduler myScheduler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCustomerByEmailId() {
        String emailID = "a@a.com";
        Customer expectedCustomer = new Customer();
        expectedCustomer.setEmailId("a@a.com");
        //Case 1 : when email id is valid
        when(customerRepo.findByEmailId(emailID)).thenReturn(Optional.of(expectedCustomer));
        Customer actualCustomer = customerServiceImp.getCustomerByEmailId(emailID);
        assertEquals(expectedCustomer.getEmailId(), actualCustomer.getEmailId());
        //Case 2 : when email id is not valid
        when(customerRepo.findByEmailId(emailID)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerServiceImp.getCustomerByEmailId(emailID));
    }


    @Test
    void getCustomerByCustomerId() {
        int ID = 1;
        Customer expectedCustomer = new Customer();
        expectedCustomer.setCustomerId(1);
        //Case 1 : When ID is valid
        when(customerRepo.findById(ID)).thenReturn(Optional.of(expectedCustomer));
        Customer actualCustomer = customerServiceImp.getCustomerByCustomerId(ID);
        assertEquals(expectedCustomer.getCustomerId(), actualCustomer.getCustomerId());
        //Case 2 : When ID is not valid
        when(customerRepo.findById(ID)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerServiceImp.getCustomerByCustomerId(ID));
    }

    @Test
    void getCustomerExpiringTodayOrTomorrow() {
        Customer expectedCustomer = new Customer();
        expectedCustomer.setCustomerId(1);
        //Case 1 : When AtLeast 1 Customer is Expiring
        when(customerRepo.findByExpireDateOrExpireDate(LocalDate.now(), LocalDate.now().plusDays(1))).thenReturn(List.of(expectedCustomer));
        List<Customer> acctualCustomerList = customerServiceImp.getCustomerExpiringTodayOrTomorrow();
        assertEquals(expectedCustomer.getCustomerId(), acctualCustomerList.get(0).getCustomerId());
        //Case 2 : When No Customer is Expiring
        when(customerRepo.findByExpireDateOrExpireDate(LocalDate.now(), LocalDate.now().plusDays(1))).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> customerServiceImp.getCustomerExpiringTodayOrTomorrow());
    }

    @Test
    void createCustomer() throws SchedulerException {
        String emailID = "a@a.com";
        int expectedCustomerID = 1;
        Customer customer = new Customer();
        customer.setEmailId(emailID);
        customer.setCustomerId(expectedCustomerID);
        Address address = new Address();
        address.setAddressId(1);
        CustomerRequestDto customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setEmailId(emailID);
        customerRequestDto.setPassword("hi");
        //Case 1 : When New Customer is Registering
        Mockito.when(passwordEncoder.encode(customerRequestDto.getPassword())).thenReturn("password");
        Mockito.when(customerRepo.findByEmailId(customerRequestDto.getEmailId())).thenReturn(Optional.empty());
        Mockito.when(addServ.addAddress(Mockito.any(Address.class))).thenReturn(address);
        Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(customer);
        when(myScheduler.intermediate(customer)).thenReturn(true);
        Customer actualCustomer = customerServiceImp.createCustomer(customerRequestDto);
        assertEquals(expectedCustomerID, actualCustomer.getCustomerId());
        //Case 1 : When EmailID is Already Exists
        when(customerRepo.findByEmailId(emailID)).thenReturn(Optional.of(customer));
        assertThrows(EntityAlreadyExistException.class, () -> customerServiceImp.createCustomer(customerRequestDto));
    }

    @Test
    void isCustomerExistsByID() {
        int customerID = 1;
        //When Customer Exists
        when(customerRepo.existsByCustomerId(customerID)).thenReturn(true);
        assertTrue(customerServiceImp.isCustomerExistsByID(customerID));
        //When Customer Does not exist
        when(customerRepo.existsByCustomerId(customerID)).thenReturn(false);
        assertFalse(customerServiceImp.isCustomerExistsByID(customerID));
    }

    @Test
    void registerCustomer() {
        Customer expectedCustomer = new Customer();
        expectedCustomer.setCustomerId(1);
        when(customerRepo.save(expectedCustomer)).thenReturn(expectedCustomer);
        assertAll(() -> customerServiceImp.registerCustomer(expectedCustomer));

    }


    @Test
    void getCustomerByEmailAndPassword() {
        String emailID = "a@a.com";
        String password = "pass";
        Customer expectedCustomer = new Customer();
        expectedCustomer.setEmailId("a@a.com");
        expectedCustomer.setPassword(password);
        //Case 1 : when email id is valid
        when(customerRepo.findByEmailIdAndPassword(emailID, passwordEncoder.encode(password))).thenReturn(Optional.of(expectedCustomer));
        Customer actualCustomer = customerServiceImp.getCustomerByEmailAndPassword(emailID, password);
        assertEquals(expectedCustomer.getEmailId(), actualCustomer.getEmailId());
        //Case 2 : when email id is not valid
        when(customerRepo.findByEmailIdAndPassword(emailID , passwordEncoder.encode(password))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerServiceImp.getCustomerByEmailAndPassword(emailID, passwordEncoder.encode(password)));
    }


//    @Test
//    void sendBulkData() throws IOException, SchedulerException {
//        byte[] validFileContent = "Sample file content".getBytes();
//        MultipartFile sampleFile = new MockMultipartFile("test.xlsx", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", validFileContent);
//        ArrayList<String> stringArrayList = new ArrayList<>();
//        stringArrayList.add("Hello");
//        when(file.getInputStream()).thenReturn(InputStream.nullInputStream());
//        when(customerServ.sheetCalc(Mockito.any(XSSFSheet.class))).thenReturn(stringArrayList);
//        ArrayList<String> arrayList = customerServiceImp.sendBulkData(sampleFile);
//        assertEquals(stringArrayList.get(0), arrayList.get(0));
//    }

    @Mock
    private Row rowMock;

    @Mock
    private Cell cellMock;

    @Test
    void getStringCellValue() {
        int cellIndex = 0;
        String expectedValue = "Test Value";
        when(rowMock.getCell(cellIndex)).thenReturn(cellMock);
        when(cellMock.getStringCellValue()).thenReturn(expectedValue);
        // Act
        String result = customerServiceImp.getStringCellValue(rowMock, cellIndex);
        // Assert
        assertEquals(expectedValue, result);
    }


}