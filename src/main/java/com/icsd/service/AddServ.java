package com.icsd.service;

import com.icsd.model.Address;

import java.util.Optional;

public interface AddServ {
	
	 Address addAddress(Address add);

	Address getByAddressId(int addressId);
}
