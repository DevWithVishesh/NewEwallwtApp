package com.icsd.serviceImp;

import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Customer;
import com.icsd.model.CustomerDocuments;
import com.icsd.repo.CustomerRepo;
import com.icsd.repo.Customerdocrepo;
import com.icsd.service.CustomerServ;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustDocServImpTest {
    @Mock
    Customerdocrepo cdrepo;
    @Mock
    CustomerServ cs;

    @InjectMocks
    CustDocServImp custDocServImp;


    @Test
    void fromCustDocId() {
        int customerDocId = 1;
        CustomerDocuments customerDocuments = new CustomerDocuments();
        customerDocuments.setDocumentuploadid(customerDocId);
        when(cdrepo.findById(customerDocId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> custDocServImp.fromCustDocId(customerDocId));
        when(cdrepo.findById(customerDocId)).thenReturn(Optional.of(customerDocuments));
        CustomerDocuments acCustomerDocuments = custDocServImp.fromCustDocId(customerDocId);
        assertEquals(customerDocuments, acCustomerDocuments);
    }
}