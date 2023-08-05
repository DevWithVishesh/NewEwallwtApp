package com.icsd.serviceImp;

import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.model.Address;
import com.icsd.repo.Addressrepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressServImpTest {
    @InjectMocks
    AddressServImp addressServImp;
    @Mock
    Addressrepo addressrepo;

    @Test
    void addAddress() {
        Address address = new Address();
        address.setAddressId(1);
        Mockito.when(addressrepo.save(address)).thenReturn(address);
        Address actualAddress = addressServImp.addAddress(address);
        assertEquals(address, actualAddress);
    }

    @Test
    void getByAddressId() {
        int addressId = 1;
        Mockito.when(addressrepo.findById(addressId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> addressServImp.getByAddressId(addressId));
        Address address = new Address();
        address.setAddressId(addressId);
        Mockito.when(addressrepo.findById(addressId)).thenReturn(Optional.of(address));
        Address actualAddress = addressServImp.getByAddressId(addressId);
        assertEquals(address, actualAddress);
    }
}