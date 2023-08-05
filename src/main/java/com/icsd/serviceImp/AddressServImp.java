package com.icsd.serviceImp;

import java.util.Optional;

import com.icsd.exceptionhand.ResourceNotFoundException;
import com.icsd.service.AddServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icsd.model.Address;
import com.icsd.repo.Addressrepo;


@Service
public class AddressServImp implements AddServ {

    @Autowired
    Addressrepo addressrepo;
    @Override
    public Address addAddress(Address add) {
        return addressrepo.save(add);
    }

    @Override
    public Address getByAddressId(int addressId) {
        Optional<Address> addressOptional= addressrepo.findById(addressId);
        if(addressOptional.isEmpty()){
            throw  new ResourceNotFoundException("No Address Of Such Id exists");
        }
        return  addressOptional.get();
    }
}
