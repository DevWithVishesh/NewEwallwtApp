package com.icsd.serviceImp;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;


import com.icsd.model.*;
import com.icsd.repo.Transrepo;
import com.icsd.scheduler.MyScheduler;
import com.icsd.service.AddServ;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.icsd.dto.request.CustomerRequestDto;
import com.icsd.exceptionhand.EntityAlreadyExistException;
import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.repo.CustomerRepo;
import com.icsd.service.CustomerServ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class CustomerServiceImp implements CustomerServ {

    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    AddServ addServ;

    @Autowired
    Transrepo TransactionRepo;
    @Autowired
    MyScheduler myScheduler;
    @Autowired
    Validator validator;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Customer getCustomerByEmailId(String strEmailId) {
        log.info("Inside getCustomer by email is" + strEmailId);
        Optional<Customer> c = customerRepo.findByEmailId(strEmailId);
        log.info("Got optional C from repo" + c);
        if (c.isPresent()) {
            return c.get();
        }
        throw new ResourceNotFoundException("No Customer of such Email Exists");
    }

    @Override
    public Customer getCustomerByCustomerId(int CustomerId) {
        log.info("Inside getCustomer by CustomerId is" + CustomerId);
        Optional<Customer> customerOptional = customerRepo.findById(CustomerId);
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        }
        throw new ResourceNotFoundException("No Customer of Such Id Exists");
    }

    @Override
    public List<Customer> getCustomerExpiringTodayOrTomorrow() {

        List<Customer> list = customerRepo.findByExpireDateOrExpireDate(LocalDate.now(), LocalDate.now().plusDays(1));
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No Customer has Expire Date Today or Tomorrow");
        }
        for (Customer customer : list
        ) {
            System.out.println(customer.getEmailId() + customer.getFirstName());
        }
        return list;
    }

    @Override
    public InputStreamResource CreateSampleSheet() throws IOException {
        String[] HEADER = {"FirstName", "LastName", "EmailId", "ContactNo", "AddressLine1", "AddressLine2"
                , "City", "State", "Pin code", "Gender", "Password", "ConfirmPassword"
        };
        String SHEET = "CustomerSampleSheet";
        XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XSSFSheet sheet = workbook.createSheet(SHEET);
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < HEADER.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(HEADER[col]);
        }
        workbook.write(out);
        InputStreamResource file = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        workbook.close();
        return file;
    }


    @Override
    public ArrayList<String> sendBulkData(MultipartFile multipartFile) throws IOException, SchedulerException {
        XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        return sheetCalc(sheet);
    }

    @Override
    public Document CreateCustomerPDF(int customerId, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(20);
        PdfPTable customerTable = new PdfPTable(2);
        customerTable.setWidthPercentage(100);
        customerTable.setSpacingBefore(10);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.BLUE);
        cell.setPadding(10);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        log.info("Before Customer");
        if (customerOptional.isPresent()) {
            Paragraph paragraph1 = new Paragraph("Customer Details", fontTitle);
            paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(paragraph1);
            Customer customer = customerOptional.get();
            cell.setPhrase(new Phrase("CustomerID", font));
            customerTable.addCell(cell);
            customerTable.addCell(String.valueOf(customer.getCustomerId()));
            cell.setPhrase(new Phrase("First Name", font));
            customerTable.addCell(cell);
            customerTable.addCell(customer.getFirstName());
            cell.setPhrase(new Phrase("Last Name", font));
            customerTable.addCell(cell);
            customerTable.addCell(customer.getLastName());
            cell.setPhrase(new Phrase("Gender", font));
            customerTable.addCell(cell);
            customerTable.addCell(String.valueOf(customer.getGender()));
            cell.setPhrase(new Phrase("EmailId", font));
            customerTable.addCell(cell);
            customerTable.addCell(customer.getEmailId());
            cell.setPhrase(new Phrase("ContactNo", font));
            customerTable.addCell(cell);
            customerTable.addCell(customer.getContactNo());
            cell.setPhrase(new Phrase("Registration Date", font));
            customerTable.addCell(cell);
            customerTable.addCell(String.valueOf(customer.getRegistrationDate()));
            cell.setPhrase(new Phrase("Expire Date", font));
            customerTable.addCell(cell);
            customerTable.addCell(String.valueOf(customer.getExpireDate()));
            document.add(customerTable);
            Paragraph addressHead = new Paragraph("Customer Address Details", fontTitle);
            addressHead.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(addressHead);
            log.info("Customer Details added Successfully");
            Address address = addServ.getByAddressId(customer.getAddress().getAddressId());
            PdfPTable addressTable = new PdfPTable(2);
            addressTable.setWidthPercentage(100);
            addressTable.setSpacingBefore(10);
            cell.setPhrase(new Phrase("Address Line 1", font));
            addressTable.addCell(cell);
            addressTable.addCell(address.getAddressLine1());
            cell.setPhrase(new Phrase("Address Line 2", font));
            addressTable.addCell(cell);
            addressTable.addCell(address.getAddressLine2());
            cell.setPhrase(new Phrase("Pin Code", font));
            addressTable.addCell(cell);
            addressTable.addCell(address.getPincode());
            cell.setPhrase(new Phrase("City", font));
            addressTable.addCell(cell);
            addressTable.addCell(address.getCity());
            cell.setPhrase(new Phrase("State", font));
            addressTable.addCell(cell);
            addressTable.addCell(address.getState());
            document.add(addressTable);
            log.info("Customer Address Details added Successfully");
            Paragraph accountHead = new Paragraph("Customer Account Details", fontTitle);
            accountHead.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(accountHead);
            List<Account> accountList = customer.getAccounts();
            PdfPTable accountTable = new PdfPTable(5);
            accountTable.setWidthPercentage(100);
            accountTable.setSpacingBefore(10);
            cell.setPhrase(new Phrase("Account Number", font));
            accountTable.addCell(cell);
            cell.setPhrase(new Phrase("Account Type", font));
            accountTable.addCell(cell);
            cell.setPhrase(new Phrase("Account Desc", font));
            accountTable.addCell(cell);
            cell.setPhrase(new Phrase("Account Balance", font));
            accountTable.addCell(cell);
            cell.setPhrase(new Phrase("Account Opening Date", font));
            accountTable.addCell(cell);
            if (accountList.isEmpty()) {
                Paragraph noAccount = new Paragraph("No Account Found", fontTitle);
                noAccount.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(noAccount);
                log.info("Customer Account Details not found");
            } else {
                for (Account account : accountList) {
                    accountTable.addCell(String.valueOf(account.getAccountNumber()));
                    accountTable.addCell(String.valueOf(account.getAccountType()));
                    accountTable.addCell(account.getDescription());
                    accountTable.addCell(String.valueOf(account.getOpeningBalance()));
                    accountTable.addCell(String.valueOf(account.getOpeningDate()));
                }
                document.add(accountTable);
                log.info("Customer Account Details added Successfully");
                document.newPage();
                log.info("New Page Added");
                Paragraph transactionHead = new Paragraph("Customer Transaction Details", fontTitle);
                transactionHead.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(transactionHead);
                List<Transaction> transactionList = TransactionRepo.findAll();
                PdfPTable transactionTable = new PdfPTable(7);
                transactionTable.setWidthPercentage(100);
                transactionTable.setSpacingBefore(10);
                cell.setPhrase(new Phrase("TransactionId", font));
                transactionTable.addCell(cell);
                cell.setPhrase(new Phrase("Amount", font));
                transactionTable.addCell(cell);
                cell.setPhrase(new Phrase("Description", font));
                transactionTable.addCell(cell);
                cell.setPhrase(new Phrase("Date", font));
                transactionTable.addCell(cell);
                cell.setPhrase(new Phrase("Transaction Type", font));
                transactionTable.addCell(cell);
                cell.setPhrase(new Phrase("From Account", font));
                transactionTable.addCell(cell);
                cell.setPhrase(new Phrase("To Account", font));
                transactionTable.addCell(cell);
                if (transactionList.isEmpty()) {
                    Paragraph noTransactionFound = new Paragraph("No Transaction Found ", fontTitle);
                    noTransactionFound.setAlignment(Paragraph.ALIGN_CENTER);
                    document.add(noTransactionFound);
                    log.info("Customer Transaction Details not found");
                } else {
                    for (Account account : accountList) {
                        for (Transaction transaction : transactionList) {
                            if (account.getAccountNumber() == transaction.getFromAccount().getAccountNumber()) {
                                transactionTable.addCell(String.valueOf(transaction.getTransactionId()));
                                transactionTable.addCell(String.valueOf(transaction.getAmount()));
                                transactionTable.addCell(transaction.getDescription());
                                transactionTable.addCell(String.valueOf(transaction.getTransactionDate()));
                                transactionTable.addCell(String.valueOf(transaction.getTransactionType()));
                                transactionTable.addCell(String.valueOf(transaction.getFromAccount().getAccountNumber()));
                                transactionTable.addCell(String.valueOf(transaction.getToAccount().getAccountNumber()));
                            }
                        }
                    }
                }
                document.add(transactionTable);
                log.info("Customer Transaction Details added Successfully");
            }
        } else {
            Paragraph noCustomer = new Paragraph("No Customer Found", fontTitle);
            noCustomer.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(noCustomer);
            log.info("Customer Details Not Found");
        }
        document.close();
        log.info("Document Closed");
        return document;
    }

    @Override
    public Customer createCustomer(CustomerRequestDto customerRequestDto) throws SchedulerException {
        log.info("Inside save customer of service with given request" + customerRequestDto);
        Optional<Customer> optCustomer = customerRepo.findByEmailId(customerRequestDto.getEmailId());
        if (optCustomer.isPresent()) {
            throw new EntityAlreadyExistException("Customer email id is already existing ");
        }
        Address add = Address.builder().addressLine1(customerRequestDto.getAddressLine1()).addressLine2(customerRequestDto.getAddressLine2()).city(customerRequestDto.getCity()).state(customerRequestDto.getState()).pincode(customerRequestDto.getPincode()).build();

        Address address = addServ.addAddress(add);

        Customer customerBuild = Customer.builder().firstName(customerRequestDto.getFirstName()).lastName(customerRequestDto.getLastName()).emailId(customerRequestDto.getEmailId()).contactNo(customerRequestDto.getContactNo()).address(address).gender(customerRequestDto.getGender()).password(passwordEncoder.encode(customerRequestDto.getPassword())).registrationDate(LocalDateTime.now()).expireDate(LocalDate.now().plusDays(5)).build();
        System.out.println("customer entity is now from builder : " + customerBuild);
log.info("before save");
        Customer customer = customerRepo.save(customerBuild);
        log.info("after save");
        myScheduler.intermediate(customerBuild);

        return customer;
    }

    @Override
    public Boolean isCustomerExistsByID(int customerID) {
        return customerRepo.existsByCustomerId(customerID);
    }

    @Override
    public Customer registerCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public Customer getCustomerByEmailAndPassword(String email, String password) {
        log.info("Inside validbyemailandpwd  of" + email + password);
        Optional<Customer> c = customerRepo.findByEmailIdAndPassword(email, passwordEncoder.encode(password));
        if (c.isPresent()) {
            return c.get();
        }
        throw new ResourceNotFoundException("Customer not found of such email,pwd");
    }

    @Override
    public ArrayList<String> sheetCalc(XSSFSheet sheet) throws SchedulerException {
        ArrayList<String> customerDtoExcelList = new ArrayList<>();
        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
            CustomerRequestDto customerDto = new CustomerRequestDto();
            Row row = sheet.getRow(i);
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                Cell ce = row.getCell(j);
                if (ce == null) {
                    j++;
                } else {
                    customerDto.setFirstName(getStringCellValue(row, 0));
                    customerDto.setLastName(getStringCellValue(row, 1));
                    customerDto.setEmailId(getStringCellValue(row, 2));
                    customerDto.setContactNo(getStringCellValue(row, 3));
                    customerDto.setAddressLine1(getStringCellValue(row, 4));
                    customerDto.setAddressLine2(getStringCellValue(row, 5));
                    customerDto.setCity(getStringCellValue(row, 6));
                    customerDto.setState(getStringCellValue(row, 7));
                    customerDto.setPincode(getStringCellValue(row, 8));
                    customerDto.setGender(Gender.valueOf(getStringCellValue(row, 9)));
                    customerDto.setPassword(getStringCellValue(row, 10));
                    customerDto.setConfirmPassword(getStringCellValue(row, 10));
                }
            }
            Optional<Customer> customerOptional = customerRepo.findByEmailId(customerDto.getEmailId());
            Set<ConstraintViolation<CustomerRequestDto>> violations = validator.validate(customerDto);
            if (!violations.isEmpty()) {
                String message = null;
                for (ConstraintViolation<CustomerRequestDto> c : violations) {
                    message = c.getMessage();
                }
                customerDtoExcelList.add("Sorry This Data Not Entered for " + " " + customerDto.getEmailId() + " " + "Because" + " " + message);
            } else if (customerOptional.isPresent()) {
                customerDtoExcelList.add("Sorry This Data Not Entered for " + " " + customerDto.getEmailId() + " " + "Because Email id already Exists");
            } else {
                createCustomer(customerDto);
                customerDtoExcelList.add("Account Added Successfully for " + " " + customerDto.getEmailId());
            }
        }
        return customerDtoExcelList;
    }

    public String getStringCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell.getStringCellValue();
    }
}